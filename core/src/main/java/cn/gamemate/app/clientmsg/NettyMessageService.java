package cn.gamemate.app.clientmsg;


import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FixedLengthFrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.handler.timeout.TimeoutException;
import org.jboss.netty.util.ExternalResourceReleasable;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

//import com.springsource.insight.annotation.InsightEndPoint;
//import com.springsource.insight.annotation.InsightOperation;

/**
 * @author jameszhang
 * 
 *         thread-safe ChannelQueue for maintaining a list of idle client
 *         channels. closed Channel will be removed out of this queue
 *         automatically.
 * 
 */
class IdleChannelQueue extends ConcurrentLinkedQueue<Channel> {
	private final ChannelFutureListener remover = new ChannelFutureListener() {
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			remove(future.getChannel());
		}
	};

	@Override
	public boolean add(Channel e) {
		if (!e.isOpen()) {
			return false;
		}
		boolean added = super.add(e);
		if (added) {
			e.getCloseFuture().addListener(remover);
		}
		return added;
	}

	@Override
	public Channel poll() {
		Channel ret = super.poll();
		if (ret != null) {
			ret.getCloseFuture().removeListener(remover);
		}
		return ret;
	}

	@Override
	public boolean remove(Object o) {
		boolean removed = super.remove(o);
		if (removed && o instanceof Channel) {
			((Channel) o).getCloseFuture().removeListener(remover);
		}
		return removed;
	}

}

/**
 * @author jameszhang <h3>Request</h3>
 * 
 *         <pre>
 * +---------------------+----------+-----------+-----------------+
 * | LJ msgsrv length(2) | MAGIC(4) | Length(2) | command type(2) | 
 * +---------------------+----------+-----------+-----------------+
 * +------------------+-------------+---------------------+-------------------+
 * | sequence num.(2) | checksum(2) | num of receivers(2) | receiver list ... |
 * +------------------+-------------+---------------------+-------------------+
 * </pre>
 * 
 * 
 *         <h3>Response</h3>
 * 
 *         <pre>
 * +---------------------+----------+-----------+-----------------+
 * | LJ msgsrv length(2) | MAGIC(4) | Length(2) | command type(2) | 
 * +---------------------+----------+-----------+-----------------+
 * +------------------+-------------+------------------+
 * | sequence num.(2) | checksum(2) | response code(2) | 
 * +------------------+-------------+------------------+
 * </pre>
 *         <ul>
 *         <li>LJ msgsrv length : content length, without self.</li>
 *         <li>magic code : constant 0xABCD</li>
 *         <li>msg length : with self, equals to LJ's msgsrv length.</li>
 *         <li>command type : constant 0x7001 for request, 0x7002 for response</li>
 *         <li>sequence number : empty</li>
 *         <li>checksum : empty</li>
 *         <li>response response code : 0-OK, 1-msgsrv error, 2-request error</li>
 *         </ul>
 * 
 * 
 *         note: all fields are encoded using <b>little endian</b>.
 */
@Configurable
public class NettyMessageService implements MessageService {

	private static final Logger logger = LoggerFactory.getLogger(NettyMessageService.class);
	

	private InetSocketAddress remoteAddr;
	private ChannelGroup allChannels;
	private ChannelFactory channelFactory;
	private ReleasableChannelPipelineFactory channelPipelineFactory;
	private final Queue<Channel> idleChannels = new IdleChannelQueue();
	private ConcurrentLinkedQueue<MessageWrapper> messages = new ConcurrentLinkedQueue<MessageWrapper>();
	final static private int recvLength = 16;

	class MessageWrapper {
		final ClientMessage message;
		final ChannelFuture future;
		int retryTimes = 0;

		public MessageWrapper(ClientMessage orginMessage, Channel channel) {
			message = orginMessage;
			future = Channels.future(channel);
		}

	}

	/**
	 * @author jameszhang
	 * 
	 *         devide response TCP Stream into 16bytes fixed-length frame:
	 */
	class MessageResponseFrameDecoder extends FixedLengthFrameDecoder {

		public MessageResponseFrameDecoder() {
			super(recvLength);
			// TODO we can store this data to ctx, preventing creating new
			// Buffer.
		}

	}

	private void pp(final byte[] buf) {
		// pretty print
		int i = 0;
		for (byte b : buf) {
			String t = Integer.toHexString(b & 0xFF).toUpperCase();
			if (t.length() > 1) {
				System.out.print(t.charAt(t.length() - 2));
			} else {
				System.out.print('0');
			}
			System.out.print(t.charAt(t.length() - 1));
			System.out.print(" ");
			i++;
			if (i >= 20) {
				System.out.println();
				i = 0;
			}
			// System.out.printf("%1H ", b);
		}
		System.out.println();
	}

	class MessageRequestFrameEncoder extends OneToOneEncoder {

		@Override
		protected Object encode(ChannelHandlerContext ctx, Channel channel,
				Object msg) throws Exception {
			final MessageWrapper messageWrapper = (MessageWrapper) msg;
			ClientMessage message = messageWrapper.message;
			byte[] content = message.getMsg().toByteArray();
			short length = (short) (16 + message.getReceivers().size() * 4 + content.length);
			ChannelBuffer buf = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN,
					length);
			buf.writeShort(length - 2);
			buf.writeInt(0xABCD);
			buf.writeShort(length - 2);
			buf.writeShort(0x7001);
			buf.writeInt(0);
			buf.writeShort(message.getReceivers().size());
			for (int r : message.getReceivers()) {
				buf.writeInt(r);
			}
			buf.writeBytes(content);
			//TODO:
			//System.out.println("trying to send: ");
			//pp(buf.array());

			return buf;
		}

	}
	
	
	
	class TimeoutHandler extends SimpleChannelHandler implements ExternalResourceReleasable{
		
		final TimeoutException EXCEPTION = new TimeoutException();
		
		private final Timer timer;
		private final long timeoutMillis;
		private MessageWrapper messageWrapper;
		private Timeout timeout;
		
		
		public TimeoutHandler(Timer timer, long timeoutMillis){
			if (timer == null){
				throw new NullPointerException("timer");
			}
			this.timer = timer;
			if (timeoutMillis <= 0){
				this.timeoutMillis = 0;
			}else{
				this.timeoutMillis = timeoutMillis;
			}
		}

		@Override
		public void releaseExternalResources() {
			timer.stop();
		}
		
		@Override
		public void writeRequested(ChannelHandlerContext ctx, final MessageEvent e){
			this.messageWrapper = (MessageWrapper)e.getMessage();
			if (timeoutMillis >0){
				final Timeout timeout = timer.newTimeout(new TimerTask(){

					@Override
					public void run(Timeout timeout) throws Exception {
						if (timeout.isCancelled()){
							return;
						}
						logger.info("message sent failed due to Timeout.");
						// if you throw a exception, other handlers can not catch it.
						messageWrapper.future.setFailure(EXCEPTION);
						e.getChannel().close();
					}
					
				}, timeoutMillis, TimeUnit.MILLISECONDS);
				this.timeout = timeout;
			}
			ctx.sendDownstream(e);
		}
		
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
			ChannelBuffer m = (ChannelBuffer) e.getMessage();
			assert m.readableBytes() == recvLength;
			timeout.cancel();
			ctx.sendUpstream(e);
		}
		
		
	}

	class MainMessageHandler extends SimpleChannelHandler {

		private final byte[] receivedMessage = new byte[recvLength];
		private MessageWrapper wrapper;

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
			Throwable ex = e.getCause();
			logger.info("exception caught: " ,ex);
			Channel ch = e.getChannel();
			if (ch.isConnected() || ch.isOpen()) ch.close();
			logger.info("Channel {} closed, all Channels: {}", ch.getId(),allChannels.size());
			if (wrapper != null){
				wrapper.future.setFailure(ex);
			}
			
		}

		@Override
		public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			MessageWrapper messageWrapper = (MessageWrapper)e.getMessage();
			wrapper = messageWrapper;
			ctx.sendDownstream(e);
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			ChannelBuffer m = (ChannelBuffer) e.getMessage();
			assert m.readableBytes() == recvLength;
			m.readBytes(receivedMessage);
			//TODO:
			//System.out.println("received: ");
			//pp(receivedMessage);
			
			int responseCode = receivedMessage[14];
			if (responseCode == 0) {
				logger.debug("Message Sent Successfully");
				wrapper.future.setSuccess();
			} else {
				logger.debug("Message Sent Failed, cause {}", responseCode);
				wrapper.future.setFailure(null);
				// TODO re-try
			}

			idleChannels.add(e.getChannel());

			// super.messageReceived(ctx, e);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.jboss.netty.channel.SimpleChannelHandler#channelConnected(org
		 * .jboss.netty.channel.ChannelHandlerContext,
		 * org.jboss.netty.channel.ChannelStateEvent)
		 * 
		 * get a message from context when connected, and send it if there is
		 * any. this method is invoked by exceptionCaught().
		 */
		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			logger.debug("Channel {} Connected", e.getChannel().getId());
			allChannels.add(e.getChannel());
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelDisconnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelDisconnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			logger.debug("Channel {} Disconnected", e.getChannel().getId());
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelClosed(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
			logger.debug("Channel {} Closed", e.getChannel().getId());
		}
		
		

	}

	
	class ReleasableChannelPipelineFactory implements ChannelPipelineFactory{
		private final Timer timer;
		
		public ReleasableChannelPipelineFactory() {		
			timer = new HashedWheelTimer(); 
		}
		
		public void close(){
			timer.stop();
		}
		
		@Override
		public ChannelPipeline getPipeline() throws Exception {
			return Channels.pipeline(
					new MessageRequestFrameEncoder(),
					new MessageResponseFrameDecoder(),
					new TimeoutHandler(timer, 10*1000),
					new MainMessageHandler());
					
		}
	}
	
	public NettyMessageService(InetSocketAddress remoteAddr) {
		ChannelFactory channelFactory = new NioClientSocketChannelFactory(Executors
				.newCachedThreadPool(), Executors.newCachedThreadPool());
		this.remoteAddr = remoteAddr;
		this.channelFactory = channelFactory;
		this.allChannels = new DefaultChannelGroup();
		this.channelPipelineFactory = new ReleasableChannelPipelineFactory();
		logger.debug("NettyMessageSender created.");
	}
	public NettyMessageService(String hostname, Integer port) {
		this(new InetSocketAddress(hostname, port));
	}
	

	private Channel getChannel() throws MessageServiceException {
		Channel ret = idleChannels.poll();
		if (ret == null) {
			ChannelPipeline pipeline;
			try {
				pipeline = channelPipelineFactory.getPipeline();
			} catch (Exception e) {
				logger.error("Failed to initialize a pipeline");
				throw new MessageServiceException(
						"Failed to initialize a pipeline");
			}
			Channel ch = channelFactory.newChannel(pipeline);
			allChannels.add(ch);
			logger.debug("New Channel created: {}, Total Channels: {}", ch.getId(), allChannels.size());
			ret = ch;
		}
		return ret;

	}

	private MessageWrapper _send(ClientMessage message){
		Channel ch;
		ch = getChannel();
		final MessageWrapper wrapper = new MessageWrapper(message, ch);

		// if ch is connected, this thread is for making the frame.
		// otherwise, a worker thread is for this responsibility.
		if (ch.isConnected()) {
			ch.write(wrapper);
		} else {
			ch.connect(remoteAddr).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future)
						throws Exception {
					future.getChannel().write(wrapper);

				}
			});
		}
		return wrapper;
	}

	/* (non-Javadoc)
	 * @see cn.gamemate.domain.message.MessageSender#send(cn.gamemate.domain.message.Message)
	 * 
	 * send a message to messageServer. blocking. thread-safe. throws MessageServiceException when failed.
	 * 
	 * 
	 */
	//@InsightOperation
	//@InsightEndPoint
	@Override
	public void send(ClientMessage message) throws MessageServiceException {

		final MessageWrapper wrapper = _send(message);

		wrapper.future.awaitUninterruptibly();
		if (!wrapper.future.isSuccess()) {
			throw new MessageServiceException("sending message Failed", wrapper.future.getCause());
		}
	}
	
	/**
	 * @param message the message to be sent.
	 * 
	 * send a message to messageServer. non-blocking. thread-safe. 
	 */
	//@InsightOperation
	//@InsightEndPoint
	public void asyncSend(ClientMessage message){

		final MessageWrapper wrapper = _send(message);
	}

	public void close() {
		allChannels.close().awaitUninterruptibly();
		channelFactory.releaseExternalResources();
		channelPipelineFactory.close();
	}
	@Override
	public void checkConnection()
			throws MessageServiceException {
		send(new UserWhisperMessage(1, 2, "HELLO"));
		
	}
}

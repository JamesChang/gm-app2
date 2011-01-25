package cn.gamemate.app.clientmsg;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jboss.netty.util.TimerTask;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.msg.MsgBase;
import proto.msg.MsgBase.Msg;

/**
 * Entity.
 * Not thread-safe.
 *
 * @author jameszhang
 */
@Configurable
public abstract class ClientMessage {  
	protected UUID uuid;
    protected String text;
    protected int ttl;
    protected DateTime expires;
    protected DateTime timeCreated;
    protected DateTime timeModified;
    protected String tag;
    
    //public String uniqueKey;
    
    protected MsgBase.Msg.Builder rootBuilder;
    protected Msg msg;
    public Msg getMsg() {
    	if (msg == null)
    		build();
		return msg;
	}

	protected final List<Integer> receivers;
	private TimerTask timerTask;
    
    private static final int defaultAge=60*60*24*30;
    //private static final int defaultCode;
    private static final boolean defaultAnswerable=false;
    private static final boolean defaultPersistent=false;
    //static final boolean unique;
    //private static final String defaultProtoname;
    //private static final String category;
    
    @Autowired(required = true)
    protected MessageService messageService;
    
	protected ClientMessage(){
		receivers = new ArrayList<Integer>();
		uuid = UUID.randomUUID();
		timeCreated = new DateTime();
		timeModified = new DateTime();
		ttl = getAge();
		expires = timeCreated.plusSeconds(getAge());
		rootBuilder = MsgBase.Msg.newBuilder();
		rootBuilder.setCode(getCode());        
	    rootBuilder.setFlagA(isAnswerable());
	    rootBuilder.setFlagP(isPersistent());
	    rootBuilder.setFlagU(false);
	    rootBuilder.setSubMsg(getProtoname());
	    rootBuilder.setTtl(getTtl());
	    rootBuilder.setUuid(uuid.toString());
	    //TODO: rootBuilder.setExpires(expires.toLocalDateTime().);
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return defaultAge;
	}

	/**
	 * @return the code
	 */
	public abstract int getCode();

	/**
	 * @return the answerable
	 */
	public boolean isAnswerable() {
		return defaultAnswerable;
	}

	/**
	 * @return the persistent
	 */
	public boolean isPersistent() {
		return defaultPersistent;
	}

	/**
	 * @return the protoname
	 */
	public String getProtoname(){
		//TDOO
		return "";
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return null;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @return the description for dev
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return Time to Live
	 */
	public int getTtl() {
		return ttl;
	}

	/**
	 * @return the inner data of protobuffer
	 */
	public void build() {
		//TODO: move msg to logging
		msg = rootBuilder.build();
		//System.out.println(msg);
		//return msg.toByteArray();
	}

	/**
	 * @return time to expire
	 */
	public DateTime getExpires() {
		return expires;
	}

	/**
	 * @return the timeCreated
	 */
	public DateTime getTimeCreated() {
		return timeCreated;
	}

	/**
	 * @return the timeModified
	 */
	public DateTime getTimeModified() {
		return timeModified;
	}

	/**
	 * @return the receivers
	 */
	public List<Integer> getReceivers() {
		return receivers;
	}
	
	public void addReceiver(Integer receiver){
		this.receivers.add(receiver);
	}
	public void addAllReceivers(List<Integer> receivers){
		this.receivers.addAll(receivers);
	}
	
	public void send(){
		if (!this.receivers.isEmpty()){
			if (messageService==null){
				throw new RuntimeException("message Service is not initialized.");
			}
			messageService.asyncSend(this);
		}
		
	}
	
	
}

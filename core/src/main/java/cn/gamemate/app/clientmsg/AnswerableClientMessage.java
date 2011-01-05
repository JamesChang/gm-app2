package cn.gamemate.app.clientmsg;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.Forbidden;
import cn.gamemate.app.domain.ObjectNotFound;
import cn.gamemate.app.domain.user.User;


@Configurable
public abstract class AnswerableClientMessage extends ClientMessage{
	
	private static final int defaultAnswerableAge=31;
	private static final Map<UUID, AnswerableClientMessage> messageRepository = 
    	new ConcurrentHashMap<UUID, AnswerableClientMessage>();

	@Autowired(required = true) 
	protected Timer timer;
	private AtomicBoolean isTimeout = new AtomicBoolean(false);
	
	protected final Set<Integer> answeredUsers = Collections.synchronizedSet(new TreeSet<Integer>());
	
	public static AnswerableClientMessage getMessage(UUID uuid, boolean mustExist){
		AnswerableClientMessage ret = messageRepository.get(uuid);
		if (mustExist && ret == null){
			throw new ObjectNotFound(AnswerableClientMessage.class, uuid);
		}
		return ret;
	}
	public static AnswerableClientMessage getMessage(UUID uuid){
		return getMessage(uuid, true);
	}
	
	
	/**
	 * @return the answerable
	 */
	@Override
	public boolean isAnswerable() {
		return true;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return defaultAnswerableAge;
	}
	
	@Override
	public void send() {
		if (this.receivers.isEmpty())
			return;
		messageRepository.put(uuid, this);
		timer.newTimeout(new TimerTask() {
			
			@Override
			public void run(Timeout timeout) throws Exception {
				isTimeout.set(true);
				AnswerableClientMessage message = messageRepository.remove(uuid);
				if (message !=null)
					onTimeOut();
				
			}
		},  getTtl(), TimeUnit.SECONDS);
		super.send();
	}

	public void answer(User user, String answer){
		if (isTimeout.get()){
			throw new DomainModelRuntimeException("expired");
		}
		if (!receivers.contains(user.getId())){
			throw new Forbidden("");
		}
		if (!answeredUsers.add(user.getId())){
			throw new DomainModelRuntimeException("User "+user.getId() + " has answered " +
					"this message");
		}
		answerCallback(user, answer);
		if (answeredUsers.size() == receivers.size()){
			messageRepository.remove(uuid);
		}
	}

	abstract protected void answerCallback(User user, String answer);
	abstract protected void onTimeOut();

}

package cn.gamemate.app.clientmsg;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.user.User;

public class CompoundMessageService implements MessageService {

	private final Logger logger;
	private final Map<String, MessageService> serviceIndex;
	private final Map<Integer, MessageService> user2service;
	private MessageService defaultService;

	public CompoundMessageService() {
		this("");
	}

	public CompoundMessageService(final String name) {
		logger = LoggerFactory.getLogger(MessageServiceInitializer.class
				.toString() + name);
		serviceIndex = new ConcurrentHashMap<String, MessageService>();
		user2service = new ConcurrentHashMap<Integer, MessageService>();
	}

	public void specifyServiceForUser(Integer uid, String serverName) {
		MessageService messageService = serviceIndex.get(serverName);
		if (messageService == null) {
			throw new DomainModelRuntimeException("service '" + serverName
					+ "' not supported.");
		}
		if (messageService != defaultService){
			user2service.put(uid, messageService);
		} 
	}
	public void clearServiceForUser(Integer uid) {
		user2service.remove(uid);
	}

	public synchronized void addService(String name, MessageService service) {
		if (defaultService == null)
			addService(name, service, true);
		else {
			addService(name, service, false);
		}
	}

	public synchronized void addService(String name, MessageService service,
			boolean isDefault) {
		serviceIndex.put(name, service);
		if (isDefault) {
			defaultService = service;
		}
	}

	@Override
	public void send(ClientMessage message) throws MessageServiceException {
		Set<MessageService> ss = null;

		for (Integer uid : message.receivers) {
			MessageService s = user2service.get(uid);
			if (s != null) {
				if (ss == null) {
					ss = new TreeSet<MessageService>();
				}
				ss.add(s);
			}
		}
		if (ss == null) {
			defaultService.asyncSend(message);
		} else {
			for (MessageService s : ss) {
				s.asyncSend(message);
			}
		}
	}

	@Override
	public void asyncSend(ClientMessage message) {
		Set<MessageService> ss = null;

		for (Integer uid : message.receivers) {
			MessageService s = user2service.get(uid);
			if (s != null) {
				if (ss == null) {
					ss = new TreeSet<MessageService>();
				}
				ss.add(s);
			}
		}
		defaultService.asyncSend(message);
		if (ss != null) {
			for (MessageService s : ss) {
				s.asyncSend(message);
			}
		}
	}

	@Override
	public void checkConnection() throws MessageServiceException {
		for (MessageService service : serviceIndex.values()) {
			service.checkConnection();
		}
	}

}

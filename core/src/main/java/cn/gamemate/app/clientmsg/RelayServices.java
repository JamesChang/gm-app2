package cn.gamemate.app.clientmsg;

import java.util.Map;
import java.util.TreeMap;

import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.user.User;

public class RelayServices {
	
	private static Map<String, MessageService> services = new TreeMap<String, MessageService>(); 
		
	
	static public void addRelayServer(String name, MessageService server){
		services.put(name, server);
	}
	
	static public MessageService getRelayService(User user){
		return services.get(user.relayService);
	}
	
	static public void spicifyRelayServiceForUser(User user, String serverName){
		MessageService messageService = services.get(serverName);
		if (messageService == null){
			throw new DomainModelRuntimeException("relay service '" + serverName +"' not found.");
		}
		user.relayService = serverName;
	}
	
}

package cn.gamemate.app.clientmsg;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserExtension;
import cn.gamemate.app.domain.user.UserRepository;

import com.google.common.base.Splitter;

@SuppressWarnings("unused")
@Configuration
public class MessageServiceInitializer {
	
	@Autowired(required=true)
	UserRepository userRepository;

	private static String HOST_PROPERTY_NAME = "msgsrv.host";
	private static String PORT_PROPERTY_NAME = "msgsrv.port";
	private Logger logger = LoggerFactory
			.getLogger(MessageServiceInitializer.class);

	@Bean
	public MessageService messageService(@Value("${msgs}") String hosts) {
		final CompoundMessageService ret=new CompoundMessageService("msgsrv");

		Iterable<String> addrs = Splitter.on(";").split(hosts);
		for(String str: addrs){
			MessageService service=null;
			String host = str.substring(0,str.indexOf(":"));
			String port = str.substring(str.indexOf(":")+1);
			try {
				service = new NettyMessageService(host, Integer.parseInt(port));
				//service.checkConnection();
				logger.info("msg server connection established. ({}:{})", host,
						port);
			} catch (Exception e) {
				logger.error(
						"Failed to start msg client: NettyMessageService({}:{})",
						host, port);
			}
			ret.addService(host, service);
		}
		userRepository.addExtension(new UserExtension(){
			@Override
			public void userLoggedOut(User user) {
				ret.clearServiceForUser(user.getId());
			}
			@Override
			public void userDrop(User user) {
				ret.clearServiceForUser(user.getId());
			}
			
		});
		
		return ret;
	}
	
	@Bean
	public RelayServices relayService(@Value("${relays}") String hosts){
		
		Iterable<String> addrs = Splitter.on(";").split(hosts);
		for(String str: addrs){
			MessageService service;
			String host = str.substring(0,str.indexOf(":"));
			String port = str.substring(str.indexOf(":")+1);
			try {
				service = new NettyMessageService(host, Integer.parseInt(port), false);
//				service.checkConnection();
			} catch (Exception e) {
				logger.error(
						"Failed to start relay client: NettyMessageService({}:{})",
						host, port);
				throw new MessageServiceException(e);
			}

			logger.info("relay server connection established. ({}:{})", host,
					port);
			RelayServices.addRelayServer(host, service);
		}
		return new RelayServices();

	}
	
	@Bean
	public Timer messageTimer(){
		return new HashedWheelTimer(500, TimeUnit.MILLISECONDS);
	}

}

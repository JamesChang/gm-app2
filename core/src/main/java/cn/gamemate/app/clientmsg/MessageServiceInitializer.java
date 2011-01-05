package cn.gamemate.app.clientmsg;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@SuppressWarnings("unused")
@Configuration
public class MessageServiceInitializer {

	private static String HOST_PROPERTY_NAME = "msgsrv.host";
	private static String PORT_PROPERTY_NAME = "msgsrv.port";
	private Logger logger = LoggerFactory
			.getLogger(MessageServiceInitializer.class);

	@Bean
	//@DependsOn("gmConfiguration")
	public MessageService messageService(
			@Value("${msgsrv.host}") String host,
			@Value("${msgsrv.port}") int port) {
		MessageService service;
		
		try {
			service = new NettyMessageService(host, port);
			service.checkConnection();
		} catch (Exception e) {
			logger.error(
					"Failed to start message client: NettyMessageService({}:{})",
					host, port);
			throw new MessageServiceException(e);
		}

		logger.info("message server connection established. ({}:{})", host,
				port);

		return service;
	}
	@Bean
	public MessageService p2pService(
			@Value("${p2psrv.host}") String host,
			@Value("${p2psrv.port}") int port) {
		MessageService service;
		
		try {
			service = new NettyMessageService(host, port);
//			service.checkConnection();
		} catch (Exception e) {
			logger.error(
					"Failed to start p2p client: NettyMessageService({}:{})",
					host, port);
			throw new MessageServiceException(e);
		}

		logger.info("message server connection established. ({}:{})", host,
				port);

		return service;
	}
	
	@Bean
	public Timer messageTimer(){
		return new HashedWheelTimer(500, TimeUnit.MILLISECONDS);
	}

}

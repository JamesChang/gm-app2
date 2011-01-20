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

import com.google.common.base.Splitter;

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
	public RelayServices relayService(@Value("${relays}") String hosts){
		
		Iterable<String> addrs = Splitter.on(";").split(hosts);
		for(String str: addrs){
			MessageService service;
			String host = str.substring(0,str.indexOf(":"));
			String port = str.substring(str.indexOf(":")+1);
			try {
				service = new NettyMessageService(host, Integer.parseInt(port));
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

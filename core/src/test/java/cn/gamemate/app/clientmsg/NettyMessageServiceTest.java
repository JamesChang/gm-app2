package cn.gamemate.app.clientmsg;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyMessageServiceTest {
	
	private Logger logger = LoggerFactory
	.getLogger(MessageServiceInitializer.class);


	@Test
	public void test(){
		
			MessageService service;
			String host = "172.16.4.1";
			String port = "60000";
			try {
				service = new NettyMessageService(host, Integer.parseInt(port), false);
				service.checkConnection();
			} catch (Exception e) {
				logger.error(
						"Failed to start relay client: NettyMessageService({}:{})",
						host, port);
				throw new MessageServiceException(e);
			}

			logger.info("relay server connection established. ({}:{})", host,
					port);
			
			
			//AlertMessage message = new AlertMessage(new User(64,"user1",""), "hello");
			//service.send(message);
			
			System.out.println("sent message");
			
			service.checkConnection();
			
			
		
	}
}

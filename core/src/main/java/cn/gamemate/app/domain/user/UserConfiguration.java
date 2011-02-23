package cn.gamemate.app.domain.user;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {
	
	@Bean
	public UserRepository userRepository(){
		UserRepository userRepository = new UserRepository();
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName objectName = new ObjectName(
					"cn.gamemate.app:name=userRepository");
			mbs.registerMBean(userRepository, objectName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return userRepository;
	}
	

}

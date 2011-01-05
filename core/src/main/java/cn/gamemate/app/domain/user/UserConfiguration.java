package cn.gamemate.app.domain.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {
	
	@Bean
	public UserRepository userRepository(){
		return new UserRepository();
	}
	

}

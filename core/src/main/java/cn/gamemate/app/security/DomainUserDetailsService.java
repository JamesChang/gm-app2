package cn.gamemate.app.security;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.gamemate.app.domain.user.User;

public class DomainUserDetailsService implements UserDetailsService{
	
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Override
	public UserDetails loadUserByUsername(String username){
		User user = User.findArenaUserByName(username);
		
		if (user == null){
			throw new UsernameNotFoundException(
					messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found"), username);
		}
		return user;
	}
	
}

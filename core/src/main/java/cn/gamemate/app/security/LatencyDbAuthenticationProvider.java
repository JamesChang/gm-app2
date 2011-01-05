package cn.gamemate.app.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

public class LatencyDbAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
	private UserDetailsService userDetailsService;
	private boolean includeDetailsObject=false;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		if (authentication.getCredentials() == null) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"),
                    includeDetailsObject ? userDetails : null);
        }

        String presentedPassword = authentication.getCredentials().toString();
        if (!checkPassword(userDetails, presentedPassword)){
        	throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"),
                    includeDetailsObject ? userDetails : null);
        }
		
	}
	
	public boolean checkPassword(UserDetails userDetails, String PresentedPassword){
		//algo, salt, hash
		String[] split = userDetails.getPassword().split("\\$");
		return split[2].equals(get_hexdigest(split[0], split[1], PresentedPassword));
	}
	

	private String get_hexdigest(String algo, String salt, String raw) {
		MessageDigest md;
		byte[] digest;
		try {
			md = MessageDigest.getInstance(algo);
			digest = md.digest((salt + raw).getBytes());
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("can not reach here");

		}
		return toHexDigest(digest);
	}
	
	private String toHexDigest(byte[] bytes){
		StringBuffer result = new StringBuffer(bytes.length*2);
		for(int i=0;i<bytes.length;i++)
		{
			String a = Integer.toHexString(bytes[i]&0xFF);
			if (a.length() <= 1) {
				result.append('0');
			}
			result.append(a);
		}
		return result.toString();
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		
		UserDetails loadedUser;

        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        }
        catch (DataAccessException repositoryProblem) {
            throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new AuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
        
	}
	

    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }
	

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }


}

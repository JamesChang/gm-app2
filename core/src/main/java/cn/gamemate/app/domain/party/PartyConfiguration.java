package cn.gamemate.app.domain.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserExtension;
import cn.gamemate.app.domain.user.UserRepository;

@Configuration
public class PartyConfiguration {


	@Autowired(required=true)
	private UserRepository userRepository;
	
	
	@Bean
	public PartyManager partyManager(){
		PartyManager partyManager = new PartyManager();
		return partyManager;
	}
	
	@Bean
	public UserExtension partyUserExtension(){
		UserExtension ux = new UserExtension(){
			@Override
			public void userLoggedOut(User user) {
				super.userLoggedOut(user);
				clearParty(user);
			}
			@Override
			public void userDrop(User user) {
				super.userDrop(user);
				clearParty(user);
			}
			private void clearParty(User user){
				PartyManager partyManager = partyManager();
				DefaultParty party = partyManager.getParty(user, false);
				if (party == null) return;
				party.removeUser(user, false);
				//new PartyLeavedMessage(party, user).send();
				new UserLeavedPartyMessage(party, user).send();
				party.autoClose();
				party.autoElectLeader(user);
			}
			
		};
		userRepository.addExtension(ux);
		return ux;
	}
}

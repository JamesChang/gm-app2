package cn.gamemate.app.domain.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.gamemate.app.domain.DomainModelRuntimeException;
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
				clearParty(user);
			}
			@Override
			public void userDrop(User user) {
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
			@Override
			public void userBrowseOnly(User user) {
				DefaultParty party = partyManager().getParty(user, false);
				if (party == null) return;
				PartyMember userSlot = party.getUserSlot(user);
				if (userSlot == null) return;
				new PartyMemberUpdateMessage(party, userSlot).send();
				party.modified();
			}
			
		};
		userRepository.addExtension(ux);
		return ux;
	}
}

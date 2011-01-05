package cn.gamemate.app.domain.party;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.user.User;

public class PartyManager {
	
	private Map<UUID, DefaultParty> partyIndex = new TreeMap<UUID, DefaultParty>();
	//private final ArrayList<PartyExtension> handlers = new ArrayList<PartyExtension>();
	
	public static void assertUserInParty(User user){
		if (!user.isInParty()){
			throw new DomainModelRuntimeException("target user is not in party");
		}
	}
	public static void assertUserNotInParty(User user){
		if (user.isInParty()){
			throw new DomainModelRuntimeException("target user is in party");
		}
	}
	//public void addExtension(PartyExtension x){
	//	handlers.add(x);
	//}
	
	public DefaultParty getParty(User user, boolean mustExist){
		UUID partyId = user.getPartyId();
		if (partyId == null){
			if (mustExist)
				throw new DomainModelRuntimeException("target user is NOT in party");
			else
				return null;
		}
		DefaultParty party =  partyIndex.get(partyId);
		if (party == null){
			if (mustExist)
				throw new DomainModelRuntimeException("target user is NOT in party");
			else
				return null;
		}
		return party;
	}
	public DefaultParty getParty(User user){
		return getParty(user, true);
	}
	

	@Autowired
	protected UUIDGenerator uuidGenerator;

	//service
	synchronized public DefaultParty userCreateParty(User leader, List<User> targets){
		assertUserNotInParty(leader);
		if (targets.size() < 1){
			return null;
		}
		DefaultParty party = new DefaultParty();
		party.uuid = uuidGenerator.generateTimeBasedUUID();
		PartyMember leaderMember = party.addUser(leader);
		leaderMember.setOut(false);
		
		for (User u : targets){
			try{
				party.addUser(u, false);
			}catch(DomainModelRuntimeException e){
				continue;
			}
		}
		party.setLeader(leader);
		
		//must have more than 1 member, Or restore user status and discard the party.
		if (party.members.size()<=1){
			return null;
		}
		partyIndex.put(party.getUuid(), party);
		new PartyJoinedMessage(party,leader.getId()).send();
		for (PartyMember m: party.members){
			if (!m.getUser().equals(leader))
				new PartyInvitationMessage(party, m.getUser()).send();
		}
		return party;
	}
	/*synchronized public DefaultParty userCreateParty(User leader, List<User> targets){
		//assertUserNotInParty(leader);
		if (targets.size() < 1){
			return null;
		}
		DefaultParty party = new DefaultParty();
		party.uuid = uuidGenerator.generateTimeBasedUUID();
		PartyMember leaderMember = party.addUser(leader);
		leaderMember.setOut(false);
		
		for (User u : targets){
			try{
				party.addUser(u);
			}catch(DomainModelRuntimeException e){
				continue;
			}
			
		}
		party.setLeader(leader);
		
		//must have more than 1 member, Or restore user status and discard the party.
		if (party.members.size()<=1){
			
			for (PartyMember m: party.members){
				try{
					m.getUser().cacPartyId(party.getUuid());
				}
				catch(DomainModelRuntimeException e){
					
				}
			}
			return null;
		}
		partyIndex.put(party.getUuid(), party);
		new PartyJoinedMessages(party,leader.getId()).send();
		for (PartyMember m: party.members){
			if (!m.getUser().equals(leader))
				new PartyInvitationMessage(party, m.getUser()).send();
		}
		return party;
	}*/
	

	public void removeIndex(DefaultParty party) {
		this.partyIndex.remove(party.getUuid());
	}
	public void sendPartyLeavedMessage(Integer userid) {
		new PartyLeavedMessage(userid).send();
	}
	
}

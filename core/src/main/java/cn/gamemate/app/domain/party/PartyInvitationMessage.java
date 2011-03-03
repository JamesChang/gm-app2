package cn.gamemate.app.domain.party;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.msg.MsgParty.PartyInvitation;
import cn.gamemate.app.clientmsg.AnswerableClientMessage;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;


@Configurable
class PartyInvitationMessage extends AnswerableClientMessage {

	static final Logger logger = LoggerFactory
			.getLogger(PartyInvitationMessage.class);

	@Autowired(required=true)
	UserRepository userRepository;

	@Autowired(required=true)
	PartyManager partyManager;
	
	DefaultParty party;

	@Override
	public int getCode() {
		return 0x2706;
	}

	@Override
	public int getAge() {
		return 31;
	}


	public PartyInvitationMessage(DefaultParty party, User target) {
		receivers.add(target.getId());
		User leader = party.getLeaderSlot().getUser();
		this.party = party;
		rootBuilder.setPartyInvitation(PartyInvitation.newBuilder()
				.setPartyID(party.getUuid().toString())
				.setLeaderID(leader.getId()).setLeaderName(leader.getName())
				.setLeaderPortrait(leader.getPortrait())
				.setTargetUserID(target.getId()));
		

	}

	@Override
	protected void answerCallback(User user, String answer) {
		if (answer.equals("yes")){
			tag = "yes";
			PartyMember userSlot = party.getUserSlot(user);
			if (userSlot == null)
				return;
			if (user.getPartyId() != null) {
				DefaultParty targetParty = partyManager.getParty(user);
				targetParty.userLeave(user);
			}
			user.casPartyId(party.uuid);
			userSlot.setOut(false);
			new PartyJoinedMessage(party, user.getId()).send();
			new PartyMemberUpdateMessage(party, userSlot).send();
		}else if (answer.equals("no")){
			tag = "no";
			party.removeUser(user, false);
			new UserLeavedPartyMessage(party, user).send();
			party.autoClose();
		}
	}

	@Override
	protected void onTimeOut() {
		if (tag !=null){
			return;
		}
		int targetUserID = getMsg().getPartyInvitation().getTargetUserID();
		User user = userRepository.getUser(targetUserID);
		party.removeUser(user, false);
		new UserLeavedPartyMessage(party, user).send();
		party.autoClose();
		
	}

}
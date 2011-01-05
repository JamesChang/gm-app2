package cn.gamemate.app.domain.party;

import proto.msg.MsgParty.UserJoinedParty;
import cn.gamemate.app.clientmsg.ClientMessage;

class UserJoinedPartyMessage extends ClientMessage {

	public UserJoinedPartyMessage(DefaultParty party, PartyMember user) {
		party.setReceivers(receivers);
		receivers.remove(Integer.valueOf(user.getUser().getId()));
		rootBuilder.setUserJoinedParty(UserJoinedParty.newBuilder()
				.setUser(user.toProtobuf())
				.setPartyID(party.getUuid().toString()));
	}

	@Override
	public int getCode() {
		return 0x2703;
	}

}
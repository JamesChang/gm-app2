package cn.gamemate.app.domain.party;

import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.user.User;

class UserLeavedPartyMessage extends ClientMessage {

	public UserLeavedPartyMessage(DefaultParty party, User user) {
		party.setReceivers(receivers);
		rootBuilder.setUserLeavedParty(proto.msg.MsgParty.UserLeavedParty
				.newBuilder().setPartyID(party.getUuid().toString())
				.setUserID(user.getId()));

	}

	@Override
	public int getCode() {
		return 0x2704;
	}

}
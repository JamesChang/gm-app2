package cn.gamemate.app.domain.party;

import proto.msg.MsgParty.PartyLeaved;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.user.User;

class PartyLeavedMessage extends ClientMessage {

	public PartyLeavedMessage(DefaultParty party, User user) {
		this.receivers.add(user.getId());
		this.rootBuilder.setPartyLeaved(PartyLeaved.newBuilder().setPartyID(
				party.getUuid().toString()));

	}
	
	public PartyLeavedMessage(Integer userid){
		this.receivers.add(userid);
		this.rootBuilder.setPartyLeaved(PartyLeaved.newBuilder().setPartyID(""));
	}

	@Override
	public int getCode() {
		return 0x2702;
	}

}
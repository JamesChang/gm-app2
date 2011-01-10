package cn.gamemate.app.domain.party;

import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.user.User;

class PartyLeaderChanged extends ClientMessage{

	public PartyLeaderChanged(DefaultParty party, User newLeader) {
		party.setReceivers(receivers);
		rootBuilder.setPartyLeaderChanged(
				proto.msg.MsgParty.PartyLeaderChanged.newBuilder()
				.setPartyID(party.getUuid().toString())
				.setLeaderID(newLeader.getId()));
	}
	public PartyLeaderChanged(DefaultParty party, Integer newLeader) {
		party.setReceivers(receivers);
		rootBuilder.setPartyLeaderChanged(
				proto.msg.MsgParty.PartyLeaderChanged.newBuilder()
				.setPartyID(party.getUuid().toString())
				.setLeaderID(newLeader));
	}
	
	@Override
	public int getCode() {
		return 0x2709;
	}
	
}
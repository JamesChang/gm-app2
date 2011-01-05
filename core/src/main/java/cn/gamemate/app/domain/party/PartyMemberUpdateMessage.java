package cn.gamemate.app.domain.party;

import proto.msg.MsgParty.PartyMemberStatusUpdated;
import proto.msg.MsgParty.PartyMemberStatusUpdated.Builder;
import cn.gamemate.app.clientmsg.ClientMessage;


public class PartyMemberUpdateMessage extends ClientMessage {
	
	
	public PartyMemberUpdateMessage(DefaultParty party, PartyMember user) {
		party.setReceivers(receivers);
		rootBuilder.setPartyMemberStatusUpdated(
				PartyMemberStatusUpdated.newBuilder()
				.setPartyID(party.getUuid().toString())
				.addUsers(user.toProtobuf()));
				
	}
	public PartyMemberUpdateMessage(DefaultParty party) {
		party.setReceivers(receivers);
		Builder builder = PartyMemberStatusUpdated.newBuilder()
		.setPartyID(party.getUuid().toString());
		for (PartyMember slot:party.getMembers()){
			builder.addUsers(slot.toProtobuf());
		}
		rootBuilder.setPartyMemberStatusUpdated(builder);
	}
	
	@Override
	public int getCode() {
		return 0x2705;
	}
}
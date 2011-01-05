package cn.gamemate.app.domain.party;

import java.util.List;

import proto.msg.MsgParty.PartyJoined;
import cn.gamemate.app.clientmsg.ClientMessage;

public class PartyJoinedMessage extends ClientMessage {

	public PartyJoinedMessage(DefaultParty party, List<Integer> receivers) {
		this.receivers.addAll(receivers);
		rootBuilder.setPartyJoined(PartyJoined.newBuilder().setParty(
				party.toProtobuf()));
	}

	public PartyJoinedMessage(DefaultParty party, Integer receiver) {
		this.receivers.add(receiver);
		rootBuilder.setPartyJoined(PartyJoined.newBuilder().setParty(
				party.toProtobuf()));
	}

	public PartyJoinedMessage(DefaultParty party) {
		party.setReceivers(receivers);
		rootBuilder.setPartyJoined(PartyJoined.newBuilder().setParty(
				party.toProtobuf()));
	}

	@Override
	public int getCode() {
		return 0x2701;
	}

}
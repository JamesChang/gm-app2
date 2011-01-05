package cn.gamemate.app.domain.party;

import java.util.List;

import proto.msg.MsgParty.EventInvitationDeclined;
import cn.gamemate.app.clientmsg.ClientMessage;

public class LadderInvitationDeclinedMessage extends ClientMessage{
	
	public LadderInvitationDeclinedMessage(List<Integer> receivers, DefaultParty party, String text) {
		this.receivers.addAll(receivers);
		rootBuilder.setEventInvitationDeclined(
				EventInvitationDeclined.newBuilder().setPartyID(party.getUuid().toString())
				.setText(text));
	}

	@Override
	public int getCode() {
		return 0x270C;
	}

}

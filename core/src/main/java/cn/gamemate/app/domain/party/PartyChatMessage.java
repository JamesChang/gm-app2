package cn.gamemate.app.domain.party;


import proto.msg.MsgChat.GroupChat;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.user.User;

public class PartyChatMessage extends ClientMessage{

	public PartyChatMessage(DefaultParty party, User sender, String content){
		party.setReceivers(receivers);
		rootBuilder.setGroupChat(
				GroupChat.newBuilder()
				.setContent(content).setSenderID(sender.getId()));
	}
	@Override
	public int getCode() {
		return 0x270A;
	}

}


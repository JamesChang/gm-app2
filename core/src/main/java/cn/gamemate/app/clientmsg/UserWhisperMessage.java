package cn.gamemate.app.clientmsg;

import proto.msg.MsgUser.WhisperMessage;

public class UserWhisperMessage extends ClientMessage {

	@Override
	public int getCode() {
		return 0x2105;
	}


	@Override
	public String getProtoname() {
		return "whisper";
	}

	public UserWhisperMessage(int sender, int receiver, String content) {
		addReceiver(receiver);
	 	rootBuilder.setWhisper(WhisperMessage.newBuilder().setContent(content)
				.setFromUserID(sender).setToUserID(receiver));
	}

}

package cn.gamemate.app.domain.user;

import proto.msg.MsgUser.UserStatusUpdate;
import cn.gamemate.app.clientmsg.ClientMessage;

public class UserStatusUpdateMessage extends ClientMessage{

	public UserStatusUpdateMessage(User user) {
		receivers.add(user.getId());
		rootBuilder.setUserStatusUpdate(
				UserStatusUpdate.newBuilder()
				.setUser(user.toProtobuf()));
	}
	
	@Override
	public int getCode() {
		return 0x2107;
	}

}

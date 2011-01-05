package cn.gamemate.app.domain.arena.msg;

import proto.msg.MsgChat.GroupChat;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.user.User;

public class ArenaChatMessage extends ClientMessage {

	public ArenaChatMessage(Arena arena, User sender, String content) {
		arena.setUserIdList(receivers);
		GroupChat.Builder builder = GroupChat.newBuilder();
		builder.setContent(content)
				.setGroupID((int) arena.getInt32Id())
				.setSenderID(sender.getId());
		rootBuilder.setGroupChat(builder);
	}

	@Override
	public int getCode() {
		return 0x2307;
	}

	@Override
	public String getProtoname() {
		return "groupChat";
	}

}

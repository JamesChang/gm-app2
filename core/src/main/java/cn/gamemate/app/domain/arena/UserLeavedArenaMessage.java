package cn.gamemate.app.domain.arena;

import proto.msg.MsgArena.UserLeavedArena;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.user.User;

public class UserLeavedArenaMessage extends ClientMessage {

	@Override
	public int getCode() {
		return 0x2304;
	}

	public UserLeavedArenaMessage(Arena arena, User user) {
		arena.setUserIdList(receivers);
		receivers.remove(Integer.valueOf(user.getId()));
		rootBuilder.setUserLeavedArena(UserLeavedArena.newBuilder()
				.setArenaID(arena.getInt32Id()).setUserID(user.getId()));

	}

}

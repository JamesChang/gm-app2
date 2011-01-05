package cn.gamemate.app.domain.arena.msg;

import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;

public class ArenaStatusUpdated extends ClientMessage{
	
	public ArenaStatusUpdated(Arena arena) {
		arena.setUserIdList(receivers);
		rootBuilder.setArenaStatusUpdated(proto.msg.MsgArena.ArenaStatusUpdated.newBuilder()
				.setArenaID(arena.getInt32Id())
				.setStatus(arena.getStatus().toString())
				.setPrivateFlag(arena.isPrivate()));
	}

	@Override
	public int getCode() {
		return 0x230C;
	}
	
}

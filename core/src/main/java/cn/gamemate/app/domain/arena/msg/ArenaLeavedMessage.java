package cn.gamemate.app.domain.arena.msg;

import proto.msg.MsgArena.ArenaLeaved;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.user.User;

public class ArenaLeavedMessage extends ClientMessage {

	@Override
	public int getCode() {
		return 0x2302;
	}
	
	public ArenaLeavedMessage(Arena arena, User user){
		this(arena.getInt32Id(), user);
	}

	public ArenaLeavedMessage(Integer id, User u) {
		addReceiver(u.getId());
		rootBuilder.setArenaLeaved(ArenaLeaved.newBuilder().setArenaID(id));
	}

}

package cn.gamemate.app.domain.arena.msg;

import proto.msg.MsgArena;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.user.User;

public class ArenaJoinedMessage extends ClientMessage {

	@Override
	public int getCode() {
		return 0x2301;
	}
	
	public ArenaJoinedMessage(Arena arena, User user){
		addReceiver(user.getId());
		MsgArena.ArenaJoined.Builder builder = MsgArena.ArenaJoined.newBuilder();
		builder.setArena(arena.toProtobuf());
		rootBuilder.setArenaJoined(builder);
	}
	
	

}

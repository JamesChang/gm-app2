package cn.gamemate.app.domain.arena.msg;

import proto.msg.MsgArena.UserJoinedArena;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.user.User;

public class UserJoinedArenaMessage extends ClientMessage {

	@Override
	public int getCode() {
		return 0x2303;
	}
	
	public UserJoinedArenaMessage(Arena arena, User user){
		arena.setUserIdList(receivers);
		receivers.remove(Integer.valueOf(user.getId()));
		rootBuilder.setUserJoinedArena(UserJoinedArena.newBuilder()
				.setArenaID(arena.getInt32Id())
				.setUserID(user.getId())
				//TODO set ca03scores 
				.setUser(user.toProtobuf())
				.setPosition(arena.getUserSlot(user).getPosition())
				//TODO set actions
				.setRole("player"));
		
		
	}

}

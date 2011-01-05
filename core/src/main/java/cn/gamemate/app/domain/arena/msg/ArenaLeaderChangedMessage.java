package cn.gamemate.app.domain.arena.msg;

import proto.msg.MsgArena.ArenaLeaderChanged;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.user.User;

public class ArenaLeaderChangedMessage extends ClientMessage {

	@Override
	public int getCode() {
		return 0x2308;
	}
	
	public ArenaLeaderChangedMessage(Arena a, User newLeader){
		a.setUserIdList(receivers);
		rootBuilder.setArenaLeaderChanged(ArenaLeaderChanged.newBuilder().setArenaID(a.getInt32Id()).setNewLeaderID(newLeader.getId()));
	}

}

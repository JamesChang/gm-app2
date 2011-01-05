package cn.gamemate.app.domain.arena.msg;

import proto.msg.MsgArena.ArenaUserAttributeUpdated;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.user.User;

public class ArenaUserAttributeUpdatedMessage extends ClientMessage {

	@Override
	public int getCode() {
		return 0x230E;
	}
	public ArenaUserAttributeUpdatedMessage(Arena arena, User operator, String key, String value){
		arena.setUserIdList(receivers);
		rootBuilder.setArenaUserAttributeUpdated(ArenaUserAttributeUpdated.newBuilder().setArenaID(arena.getInt32Id()).setKey(key).setValue(value).setUserID(operator.getId()));
	}

}

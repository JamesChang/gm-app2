package cn.gamemate.app.domain.arena.msg;

import proto.msg.MsgArena.ArenaSlotLockUpdated;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.ArenaSlot;

public class ArenaSlotLockUpdatedMessage extends ClientMessage{
	
	public ArenaSlotLockUpdatedMessage(Arena arena, ArenaSlot slot){
		arena.setUserIdList(receivers);
		rootBuilder.setArenaSlotLockUpdated(ArenaSlotLockUpdated.newBuilder()
				.setArenaID(arena.getInt32Id()).setSlotID(slot.getPosition()).setEnabled(slot.isEnabled()));
	}

	@Override
	public int getCode() {
		return 0x2314;
	}

}

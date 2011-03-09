package cn.gamemate.app.domain.arena.msg;

import java.util.List;

import proto.msg.MsgArena.ArenaMemberUpdated;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.ArenaSlot;
import cn.gamemate.app.domain.user.User;

public class ArenaMemberUpdatedMessage extends ClientMessage {

	@Override
	public int getCode() {
		return 0x2305;
	}

	/**
	 * note: readyChanged should always be true, cause flash's proto decoder can
	 * not distinguish setting false from not set at all.
	 * 
	 * @param arena
	 * @param user
	 * @param statusChanged
	 * @param positionChanged
	 * @param actionChanged
	 * @param readyChanged
	 */
	public ArenaMemberUpdatedMessage(Arena arena, User user,
			boolean statusChanged, boolean positionChanged,
			boolean actionChanged, boolean readyChanged) {
		arena.setUserIdList(receivers);
		
		ArenaMemberUpdated.Builder builder = ArenaMemberUpdated.newBuilder();
		builder.setArenaID(arena.getInt32Id()).setUserID(user.getId());
		ArenaSlot userSlot = arena.getUserSlot(user);
		if (userSlot==null) return;
		
		if (statusChanged){
			builder.setUserStatus(userSlot.isGaming()?"gaming":"waiting");
		}
		if (positionChanged)
			builder.setPosition(arena.getUserSlot(user).getPosition());
		else
			builder.setPosition(-1);

		if (actionChanged) {
			List<String> actions = arena.getUserAvailableActions(user);
			for(String s : actions){
				builder.addActions(s);
			}
		}

		if (readyChanged) {
			builder.setReady(arena.getUserSlot(user).isReady());
		}
		rootBuilder.setArenaMemberUpdated(builder);

	}

}

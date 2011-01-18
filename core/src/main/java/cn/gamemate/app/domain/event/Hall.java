package cn.gamemate.app.domain.event;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.user.User;

abstract public class Hall extends Event{
	
	

	abstract public DomainModel getArenaList();
	abstract public void partyCreateArena(User operator, String mode, Integer mapId,
			String customName, boolean isPrivate);
	abstract public void partyCreateArena(User operator, String mode, Integer mapId);
	
	/**
	 * A user creates an arena. the arena created is indexed by this Event.
	 * @param operator
	 * @param mode
	 * @param mapId
	 * @param customName
	 * @param isPrivate
	 * @return the arena created.
	 */
	abstract public Arena05 userCreateArena(User operator, String mode,
			Integer mapId, String customName, boolean isPrivate);
	
	public Arena05 userCreateArena(User operator, String mode,
			Integer mapId, boolean isPrivate) {
		return userCreateArena(operator, mode, mapId,
				getDefaultArenaName(operator), isPrivate);
	}

	public Arena05 userCreateArena(User operator, String mode,
			Integer mapId, String customName) {
		return userCreateArena(operator, mode, mapId, customName, false);
	}
	
	public Arena05 userCreateArena(User operator, String mode,
			Integer mapId) {
		return userCreateArena(operator, mode, mapId,
				getDefaultArenaName(operator), false);
	}

	protected String getDefaultArenaName(User operator) {
		return operator.getName() + "的房间";
	}
	
}

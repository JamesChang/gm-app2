package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.List;

import proto.response.ResEvent;
import proto.response.ResGame;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.arena.DefaultArenaBuilder;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.common.annotation.ThreadSafe;

@ThreadSafe
abstract public class Hall extends Event implements ReplaySupport{
	
	private boolean replayRequired = false;

	/**
	 * immutable after initialization. 
	 */
	protected Game game;
	
	
	/**
	 * immutable after initialization. 
	 */
	final List<GameMap> gameMaps = new ArrayList<GameMap>();
	
	/**
	 * Template of ArenaBuilder. immutable after initialization. 
	 */
	protected DefaultArenaBuilder arenaBuilder;
	
	/**
	 * @return the arenaBuilder
	 */
	public synchronized DefaultArenaBuilder getArenaBuilder() {
		return arenaBuilder;
	}

	/**
	 * @param arenaBuilder
	 *            the arenaBuilder to set
	 */
	public void setArenaBuilder(DefaultArenaBuilder arenaBuilder) {
		this.arenaBuilder = arenaBuilder;
	}

	private void assertMapAllowed(Integer mapId){
		Long longMapId = new Long(mapId);
		boolean allowed = false;
		for(GameMap m :gameMaps){
			if (m.getId().equals(longMapId)){
				allowed = true;
				break;
			}
		}
		if (!allowed) throw new DomainModelRuntimeException("This map is not allowed.");
	}

	abstract public DomainModel getArenaList(String stick);

	abstract public void partyCreateArena(User operator, String mode,
			Integer mapId, String customName, boolean isPrivate);


	public void partyCreateArena(User operator, String mode,
			Integer mapId) {
		partyCreateArena(operator, mode, mapId,
				getDefaultArenaName(operator), false);
	}

	/**
	 * A user creates an arena. the arena created is indexed by this Event.
	 * 
	 * @param operator
	 * @param mode
	 * @param mapId
	 * @param customName
	 * @param isPrivate
	 * @return the arena created.
	 */
	synchronized public Arena05 userCreateArena(User operator, String mode,
			Integer mapId, String customName, boolean isPrivate){
		
		Arena05.assertUserNotInArena(operator);
		assertAvailable(operator);

		Arena05 arena;
		if (mapId != null) {
			assertMapAllowed(mapId);
		//	DefaultArenaBuildearenaBuilderr builder = (DefaultArenaBuilder) arenaBuilder
		//			.copy();
			arenaBuilder.setGameMap(mapId).bisectSlots();
			arena = (Arena05) arenaBuilder.newArena();
		} else {
			if (arenaBuilder.getGameMap()== null){
				throw new DomainModelRuntimeException("game map must be specified.");
			}
			arena = (Arena05) arenaBuilder.newArena();
		}
		if (customName == null || customName.equals("")) {
			customName = getDefaultArenaName(operator);
		}
		arena.setName(customName);
		arena.setMode(mode);
		arena.setEvent(this);
		arena.userStatusEx = this.name;
		
		if (isReplayRequired()){
			arena.getAttributes().put("replay_required", Boolean.toString(isReplayRequired()));
		}

		// TODO arena.setPrivate
		arena.save();
		arena.addPlayer(operator);
		arena.setLeader(operator);

		operator.setArenaId(arena.getInt32Id());
		return arena;
	}

	public Arena05 userCreateArena(User operator, String mode, Integer mapId,
			boolean isPrivate) {
		return userCreateArena(operator, mode, mapId,
				getDefaultArenaName(operator), isPrivate);
	}

	public Arena05 userCreateArena(User operator, String mode, Integer mapId,
			String customName) {
		return userCreateArena(operator, mode, mapId, customName, false);
	}

	public Arena05 userCreateArena(User operator, String mode, Integer mapId) {
		return userCreateArena(operator, mode, mapId,
				getDefaultArenaName(operator), false);
	}

	protected String getDefaultArenaName(User operator) {
		return operator.getName() + "的房间";
	}
	

	@Override
	public ResEvent.EventGet.Builder toProtobuf(int verbose) {
		ResEvent.EventGet.Builder builder = ResEvent.EventGet.newBuilder();
		builder.setName(name).setId(id);
		if (verbose < 1){
			return builder;
		}
		
		builder.setPhysicalGame(game.getPhysicalGame().toProtobuf());
		builder.setLogicalGame(game.toProtobuf());
		if (verbose < 2){
			return builder;
		}
		
		for (GameMap map : gameMaps){
			builder.addOptionalMaps(map.toProtobuf());
		}
		return builder;
	}
	
	public void setReplayRequired(boolean replayRequired) {
		this.replayRequired = replayRequired;
	}

	@Override
	public boolean isReplayRequired() {
		return replayRequired;
	}

}

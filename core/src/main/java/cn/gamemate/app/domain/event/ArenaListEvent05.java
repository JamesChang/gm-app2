package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.res.ResArena;
import proto.response.ResGame;
import proto.response.ResCampusArena.CampusArena03List;
import proto.response.ResCampusArena.CampusArena03ListItem;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Arena.ArenaStatus;
import cn.gamemate.app.domain.arena.ArenaClosedEvent;
import cn.gamemate.app.domain.arena.ArenaExtension;
import cn.gamemate.app.domain.arena.ArenaStatusChangedEvent;
import cn.gamemate.app.domain.arena.DefaultArenaBuilder;
import cn.gamemate.app.domain.arena.msg.ArenaInvitationMessageEx;
import cn.gamemate.app.domain.arena.msg.ArenaJoinedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaMemberUpdatedMessage;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.party.DefaultParty;
import cn.gamemate.app.domain.party.PartyManager;
import cn.gamemate.app.domain.party.PartyMember;
import cn.gamemate.app.domain.user.User;


@Configurable
public class ArenaListEvent05 extends Hall{

	final List<GameMap> gameMaps = new ArrayList<GameMap>();
	private Map<Integer, Arena05> arenaIndex = new ConcurrentHashMap<Integer, Arena05>();
	private DefaultArenaBuilder arenaBuilder;
	protected Game game;
	
	@Autowired(required=true)
	private PartyManager partyManager;


	/**
	 * @return the arenaBuilder
	 */
	public DefaultArenaBuilder getArenaBuilder() {
		return arenaBuilder;
	}

	/**
	 * @param arenaBuilder
	 *            the arenaBuilder to set
	 */
	public void setArenaBuilder(DefaultArenaBuilder arenaBuilder) {
		this.arenaBuilder = arenaBuilder;
	}

	class ArenaList implements DomainModel {

		@Override
		public CampusArena03List.Builder toProtobuf() {
			CampusArena03List.Builder pbList = CampusArena03List.newBuilder();
			for (Arena05 arena : arenaIndex.values()) {
				pbList.addItems(CampusArena03ListItem
						.newBuilder()
						.setId(arena.getInt32Id())
						// TODO set uuid
						.setName(arena.getName()).setMode(arena.getMode())
						.setMapid(arena.getGameMap().getId().intValue())
						.setMapname(arena.getGameMap().getName())
						.setMcount(arena.getSlotCountString())
						.setLeaderid(arena.getLeader().getId()));
			}
			return pbList;
		}

	}

	private void updateIndex(Arena arena) {
		arenaIndex.put(arena.getInt32Id(), (Arena05) arena);
	}

	private void removeIndex(Arena arena) {
		arenaIndex.remove(arena.getInt32Id());
	}
	
	
	synchronized public void partyCreateArena(User operator, String mode, Integer mapId,
			String customName, boolean isPrivate
			){
		DefaultParty party = partyManager.getParty(operator);
		if (party == null){
			throw new DomainModelRuntimeException("Not in party");
		}
		if (customName == null || customName.equals("")){
			customName = getDefaultArenaName(operator);
		}
		synchronized(party){
			party.assertPartyLeader(operator);
			for (PartyMember partyMember: party.getMembers()){
				if (partyMember.isOut())
					throw new DomainModelRuntimeException(partyMember.getUser().getName()+" is out");
			}
			acquireParty(party);
			new ArenaInvitationMessageEx(party, this, mode, mapId,
					customName, isPrivate).send();
		}
	}
	synchronized public void partyCreateArena(User operator, String mode,
			Integer mapId) {
		partyCreateArena(operator, mode, mapId,
				getDefaultArenaName(operator), false);
	}
	

	synchronized public Arena05 userCreateArena(User operator, String mode,
			Integer mapId, String customName, boolean isPrivate) {
		// TODO check parameters
		Arena05.assertUserNotInArena(operator);
		assertAvailable(operator);
		
			Arena05 arena;
			if (mapId != null) {
				DefaultArenaBuilder builder = (DefaultArenaBuilder) arenaBuilder
						.copy();
				builder.setGameMap(mapId).bisectSlots();
				arena = (Arena05) builder.newArena();
			} else {
				arena = (Arena05) arenaBuilder.newArena();
			}
			if (customName == null || customName.equals("")){
				customName = getDefaultArenaName(operator);
			}
			arena.setName(customName);
			arena.setMode(mode);
			arena.setEvent(this);
			arena.userStatusEx = this.name;
	
			// TODO arena.setPrivate
			arena.save();
			arena.addPlayer(operator);
			arena.setLeader(operator);
			
			operator.setArenaId(arena.getInt32Id());
			arena.addExtension(new ArenaExtension() {
				@Override
				public void closed(ArenaClosedEvent e) {
					removeIndex(e.getModel());
				}
	
				@Override
				public void statusChanged(ArenaStatusChangedEvent e) {
					if (e.getModel().getStatus() == ArenaStatus.OPEN) {
						updateIndex(e.getModel());
					} else {
						removeIndex(e.getModel());
					}
	
				}
	
			});

			new ArenaJoinedMessage(arena, operator).send();
			new ArenaMemberUpdatedMessage(arena, operator, false, false, true,
					false).send();
			updateIndex(arena);
			return arena;
	}
	synchronized public DomainModel getArenaList() {
		return new ArenaList();
	}

	@Override
	public ResGame.EventGet.Builder toProtobuf() {
		ResGame.EventGet.Builder builder = ResGame.EventGet.newBuilder();
		builder.setName(name).setId(id);
		builder.setPhysicalGame(game.getPhysicalGame().toProtobuf());
		for (GameMap map : gameMaps){
			builder.addOptionalMaps(map.toProtobuf());
		}
		return builder;
	}

}

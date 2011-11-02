package cn.gamemate.app.domain.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.response.ResCampusArena.CampusArena03List;
import proto.response.ResCampusArena.CampusArena03ListItem;
import proto.response.ResEvent;
import proto.response.ResGame;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Arena.ArenaStatus;
import cn.gamemate.app.domain.arena.ArenaClosedEvent;
import cn.gamemate.app.domain.arena.ArenaExtension;
import cn.gamemate.app.domain.arena.ArenaStatusChangedEvent;
import cn.gamemate.app.domain.arena.msg.ArenaInvitationMessageEx;
import cn.gamemate.app.domain.arena.msg.ArenaJoinedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaMemberUpdatedMessage;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.party.DefaultParty;
import cn.gamemate.app.domain.party.PartyManager;
import cn.gamemate.app.domain.party.PartyMember;
import cn.gamemate.app.domain.user.AlertMessage;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.common.annotation.ThreadSafe;

@ThreadSafe
@Configurable
public class SimpleHall extends Hall {

	@Autowired(required = true)
	private PartyManager partyManager;

	private final Map<Integer, Arena05> arenaIndex = new ConcurrentHashMap<Integer, Arena05>();

	class ArenaList implements DomainModel {

		@Override
		public CampusArena03List.Builder toProtobuf() {
			CampusArena03List.Builder pbList = CampusArena03List.newBuilder();
			for (Arena05 arena : arenaIndex.values()) {
				pbList.addItems(CampusArena03ListItem
						.newBuilder()
						.setId(arena.getInt32Id())
						.setUuid(arena.getUuid().toString())
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

	synchronized public void partyCreateArena(User operator, String mode,
			Integer mapId, String customName, boolean isPrivate) {
		DefaultParty party = partyManager.getParty(operator);
		if (party == null) {
			throw new DomainModelRuntimeException("Not in party");
		}
		if (customName == null || customName.equals("")) {
			customName = getDefaultArenaName(operator);
		}
		synchronized (party) {
			party.assertPartyLeader(operator);
			for (PartyMember partyMember : party.getMembers()) {
				if (partyMember.isOut())
					throw new DomainModelRuntimeException(partyMember.getUser()
							.getName() + " is out");
			}
			acquireParty(party);
			new ArenaInvitationMessageEx(party, this, mode, mapId, customName,
					isPrivate).send();
		}
	}

	synchronized public void partyCreateArena(User operator, String mode,
			Integer mapId) {
		partyCreateArena(operator, mode, mapId, getDefaultArenaName(operator),
				false);
	}

	synchronized public Arena05 userCreateArena(User operator, String mode,
			Integer mapId, String customName, boolean isPrivate) {
		//check relay
		if (operator.getRelayServiceName() == null){
			new AlertMessage(operator, "无法确认游戏服务器。请检查您的网络连接", true).send();
			throw new DomainModelRuntimeException("your status is not online.");
		}

		Arena05 arena = super.userCreateArena(operator, mode, mapId,
				customName, isPrivate);
		arena.addExtension(new ArenaExtension() {
			@Override
			public void closed(ArenaClosedEvent e) {
				removeIndex(e.getModel());
			}

			@Override
			public void statusChanged(ArenaStatusChangedEvent e) {
				if (e.getModel().getStatus() == ArenaStatus.OPEN && 
						!e.getModel().isPrivate()) {
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

	@Override
	public DomainModel getArenaList(String stick) {
		return new ArenaList();
	}

	@Override
	public ResEvent.EventGet.Builder toProtobuf() {
		ResEvent.EventGet.Builder builder = ResEvent.EventGet.newBuilder();
		builder.setName(name).setId(id);
		builder.setPhysicalGame(game.getPhysicalGame().toProtobuf());
		for (GameMap map : gameMaps) {
			builder.addOptionalMaps(map.toProtobuf());
		}
		return builder;
	}

}

package cn.gamemate.app.domain.event;

import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.response.ResCampusArena.CampusArena03List;
import proto.response.ResCampusArena.CampusArena03ListItem;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.ArenaClosedEvent;
import cn.gamemate.app.domain.arena.ArenaExtension;
import cn.gamemate.app.domain.arena.ArenaStatusChangedEvent;
import cn.gamemate.app.domain.arena.Arena.ArenaStatus;
import cn.gamemate.app.domain.arena.msg.ArenaJoinedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaMemberUpdatedMessage;
import cn.gamemate.app.domain.party.PartyManager;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.common.annotation.GuardedBy;
import cn.gamemate.common.annotation.ThreadSafe;

@ThreadSafe
@Configurable
public class BigHall extends Hall {

	@Autowired(required = true)
	private PartyManager partyManager;

	// @Autowired(required = true)
	// protected Timer timer;
	protected java.util.Timer timer = new java.util.Timer();

	private final PriorityBlockingQueue<Cell> allCells;
	private final int MERGING_INTERVAL = 3;

	public BigHall() {
		allCells = new PriorityBlockingQueue<Cell>();
		allCells.add(new Cell());
	}

	public void start() {
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				mergeCells();
			}

		}, MERGING_INTERVAL, MERGING_INTERVAL);
	}

	class Cell extends CopyOnWriteArrayList<Arena>{
		/**
		 * this cell splits into two, in another word, copy.
		 * 
		 * @return a shadow copy
		 */
		public Cell split() {
			return null;
		}

		/**
		 * merge from another cell.
		 * 
		 * @param target
		 */
		public void merge(Cell target) {

		}
		
		public DomainModel getArenaList() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	private void mergeCells() {

	}
	
	@GuardedBy("this")
	private synchronized void addArena(Arena arena){
		Cell cell = allCells.poll();
		cell.add(arena);
		allCells.offer(cell);
	}

	@Override
	public DomainModel getArenaList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void partyCreateArena(User operator, String mode, Integer mapId,
			String customName, boolean isPrivate) {
		// TODO Auto-generated method stub

	}

	@Override
	public Arena05 userCreateArena(User operator, String mode, Integer mapId,
			String customName, boolean isPrivate) {
		Arena05 arena = super.userCreateArena(operator, mode, mapId,
				customName, isPrivate);
		new ArenaJoinedMessage(arena, operator).send();
		new ArenaMemberUpdatedMessage(arena, operator, false, false, true,
				false).send();
		addArena(arena);
		return arena;
	}
	

	private class ArenaList implements DomainModel {
		
		private CampusArena03List.Builder builder;
		
		public ArenaList(Cell cell) {
			CampusArena03List.Builder pbList = CampusArena03List.newBuilder();
			for (Arena arena : cell) {
				pbList.addItems(CampusArena03ListItem
						.newBuilder()
						.setId(arena.getInt32Id())
						// TODO set uuid
						.setName(arena.getName()).setMode(arena.getMode())
						.setMapid(arena.getGameMap().getId().intValue())
						.setMapname(arena.getGameMap().getName())
						.setMcount(arena.getSlotCountString())
						.setLeaderid(((Arena05)arena).getLeader().getId()));
			}
			builder = pbList;
		}

		@Override
		public CampusArena03List.Builder toProtobuf() {
			return builder
		}

	}

}

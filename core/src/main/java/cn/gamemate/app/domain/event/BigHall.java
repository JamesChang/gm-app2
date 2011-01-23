package cn.gamemate.app.domain.event;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.response.ResCampusArena.CampusArena03List;
import proto.response.ResCampusArena.CampusArena03ListItem;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.arena.Arena;
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

	private final Map<Integer,Cell> allCells;
	private final int MERGING_INTERVAL = 3;
	private static final AtomicInteger lastKey = new AtomicInteger();
	private final int SPLITTING_THREAHOLD = 10;
	

	public BigHall() {
		allCells = new ConcurrentHashMap<Integer, Cell>();
		new Cell().live();
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
		
		private final Integer key;
		
		public Cell() {
			key = lastKey.incrementAndGet();
		}
		public Cell(Cell other){
			this();
			this.addAll(other);
		}
		
		public void live(){
			allCells.put(key, this);
		}
		public void die(){
			allCells.remove(key);
		}
		
		/**
		 * this cell splits into two, in another word, copy.
		 * 
		 * @return a shadow copy
		 */
		public Cell split() {
			Cell n = new Cell(this);
			return n;
		}

		/**
		 * merge from another cell.
		 * 
		 * @param target
		 */
		public void merge(Cell target) {

		}
		
		@Override
		public boolean add(Arena e) {
			if (size() > SPLITTING_THREAHOLD){
				Cell bro = split();
				bro.live();
			}
			return super.add(e);
		}
		
	}

	private void mergeCells() {

	}
	
	@GuardedBy("this")
	private Iterator<Cell> cellLastIterator;
	
	@GuardedBy("this")
	private synchronized void addArena(Arena arena){
		if (cellLastIterator == null || !cellLastIterator.hasNext()){
			cellLastIterator = allCells.values().iterator();
		}
		Cell cell = cellLastIterator.next();
		cell.add(arena);
	}

	@Override
	public DomainModel getArenaList(String stick) {
		Cell targetCell = null;
		if (stick != null){
			targetCell = allCells.get(new Integer(stick));
		}
		if (targetCell == null){
			Collection<Cell> cells = allCells.values();
			int t = new Random().nextInt(cells.size());
			int i=0;
			for (Cell c: cells){
				if (i++ == t){
					targetCell = c;
					break;
				}
			}
		}
		return new ArenaList(targetCell);
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
			return builder;
		}

	}

}

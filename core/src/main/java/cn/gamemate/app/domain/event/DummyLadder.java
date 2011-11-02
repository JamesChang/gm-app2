package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import proto.response.ResEvent;
import proto.response.ResGame;

import com.google.protobuf.GeneratedMessage.Builder;

import cn.gamemate.app.domain.NotFullySupportedException;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Arena.ArenaStatus;
import cn.gamemate.app.domain.arena.ArenaClosedEvent;
import cn.gamemate.app.domain.arena.ArenaExtension;
import cn.gamemate.app.domain.arena.ArenaMerger;
import cn.gamemate.app.domain.arena.ArenaSlot;
import cn.gamemate.app.domain.arena.ArenaStatusChangedEvent;
import cn.gamemate.app.domain.arena.DefaultArenaBuilder;
import cn.gamemate.app.domain.arena.msg.ArenaJoinedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaMemberUpdatedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaStartMessage;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.User;


class PartialArenaBuilder extends DefaultArenaBuilder{
	
	@Override
	protected Arena05 _newArena() {
		return new PartialArena05();
	}
	
}



public class DummyLadder extends Ladder{
	
	private DefaultArenaBuilder partialBuilder;
	private ArenaMerger finalBuilder;

	private Arena lastArena = null;
	private final List<GameMap> maps = new ArrayList<GameMap>();
	private Random random = new Random();
	private Integer m;
	private String mode;
	
	
	/**
	 * @return the m
	 */
	public Integer getM() {
		return m;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @param m the m to set
	 */
	public void setM(Integer m) {
		this.m = m;
	}

	public void setFinalArenaBuilder(ArenaMerger arenaBuilder){
		this.finalBuilder = arenaBuilder;
	}
	
	public List<GameMap> getMaps(){
		return maps;
	}

	/**
	 * @param arenaBuilder the arenaBuilder to set
	 */
	public void setPartialArenaBuilder(DefaultArenaBuilder arenaBuilder) {
		this.partialBuilder = arenaBuilder;
	}
	
	
	private void updateIndex(Arena arena){
		if (lastArena == null){
			lastArena = arena;
		}else{
			if (!lastArena.equals(arena)){
				Arena finalArena = makeGame(lastArena, arena);
				lastArena = null;
				new ArenaStartMessage(finalArena).send();
			}
		}
	}
	
	/**
	 * copy two arena into one new Arena 
	 */
	protected Arena makeGame(Arena one, Arena two){
		GameMap map = maps.get(random.nextInt(maps.size()));
		finalBuilder.setArenas(one, two);
		Arena arena = finalBuilder.newArena();
		arena.setGameMap(map);
		User leader = null;
		for(ArenaSlot slot:arena.getSlots()){
			if (slot.getUser()!=null){
				leader = slot.getUser();
				break;
			}
		}
		assert leader != null;
		arena.getAttributes().put("hostID", String.valueOf(leader.getId()));
		arena.setName(leader.getName() + "的房间");
		return arena;
	}
	
	private void removeIndex(Arena arena){
		if (lastArena == arena){
			lastArena = null;
		}
	}
	
	synchronized public Arena05 userCreateArena(User operator,
			String mode) {
		
		Arena05 arena;
		DefaultArenaBuilder builder = (DefaultArenaBuilder) partialBuilder.copy();
		//builder.setGameMap(6);
		builder.setSlotNum(m, 0);
		arena = (Arena05)builder.newArena();
		arena.save();
		arena.addPlayer(operator);
		arena.setLeader(operator);
		arena.setMode(getMode());
		arena.addExtension(new ArenaExtension(){
			@Override
			public void closed(ArenaClosedEvent e) {
				
			}
			@Override
			public void statusChanged(ArenaStatusChangedEvent e) {
				if (e.getModel().getStatus() == ArenaStatus.MATCHING){
					updateIndex(e.getModel());
				}else{
					removeIndex(e.getModel());
				}
					
			}
			
		});
		operator.setArenaId(arena.getInt32Id());
		new ArenaJoinedMessage(arena, operator).send();
		new ArenaMemberUpdatedMessage(arena, operator, false, false, true, false).send();
		//TODO FriendDataChanged
		return arena;
		
	}

	@Override
	public ResEvent.EventGet.Builder toProtobuf(int verbose) {
		throw new NotFullySupportedException("");
	}

}

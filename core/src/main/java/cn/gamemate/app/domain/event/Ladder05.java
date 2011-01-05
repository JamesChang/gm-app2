package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import proto.response.ResGame;

import com.google.protobuf.GeneratedMessage.Builder;

import cn.gamemate.app.domain.NotFullySupportedException;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Arena.ArenaStatus;
import cn.gamemate.app.domain.arena.ArenaClosedEvent;
import cn.gamemate.app.domain.arena.ArenaExtension;
import cn.gamemate.app.domain.arena.ArenaSlot;
import cn.gamemate.app.domain.arena.ArenaStatusChangedEvent;
import cn.gamemate.app.domain.arena.DefaultArenaBuilder;
import cn.gamemate.app.domain.arena.UserSlot;
import cn.gamemate.app.domain.arena.msg.ArenaJoinedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaMemberUpdatedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaStartMessage;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.User.UserStatus;



public class Ladder05 extends Ladder{
	
	protected GreedyMatcher matcher;
	private DefaultArenaBuilder partyBuilder;
	private DefaultArenaBuilder finalBuilder;
	private final List<GameMap> maps = new ArrayList<GameMap>();
	private Integer m;
	private String mode;
	private Random random = new Random();
	private final int TICK_INTERVAL = 1000;
	private java.util.Timer timer;
	private String name ;
	
	

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Ladder05() {
		
	}
	
	/**
	 * @return the matcher
	 */
	public GreedyMatcher getMatcher() {
		return matcher;
	}

	public void start(){
		timer = new java.util.Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				matcher.tick();
			}
			
		}, 
		TICK_INTERVAL,
		TICK_INTERVAL);
	}

	/**
	 * @param matcher the matcher to set
	 */
	public void setMatcher(GreedyMatcher matcher) {
		this.matcher = matcher;
		matcher.addHandler(new MatcherHandler(){
			@Override
			public void onMatched(MatcherContext e) {
				Arena finalArena = makeGame(e);
				new ArenaStartMessage(finalArena).send();
			}
		});
	}



	/**
	 * @return the partyBuilder
	 */
	public DefaultArenaBuilder getPartyBuilder() {
		return partyBuilder;
	}



	/**
	 * @param partyBuilder the partyBuilder to set
	 */
	public void setPartyBuilder(DefaultArenaBuilder partyBuilder) {
		this.partyBuilder = partyBuilder;
	}



	/**
	 * @return the finalBuilder
	 */
	public DefaultArenaBuilder getFinalBuilder() {
		return finalBuilder;
	}



	/**
	 * @param finalBuilder the finalBuilder to set
	 */
	public void setFinalBuilder(DefaultArenaBuilder finalBuilder) {
		this.finalBuilder = finalBuilder;
	}



	/**
	 * @return the m
	 */
	public Integer getM() {
		return m;
	}



	/**
	 * @param m the m to set
	 */
	public void setM(Integer m) {
		this.m = m;
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
	 * @return the maps
	 */
	public List<GameMap> getMaps() {
		return maps;
	}


	private void updateIndex(Arena arena){
		matcher.add((Party)arena);
	}

	private void removeIndex(Arena arena){
		matcher.remove((Party)arena);
	}

	/**
	 * copy two arena into one new Arena 
	 * @param context 
	 */
	protected Arena makeGame(MatcherContext context){
		
		
		GameMap map = maps.get(random.nextInt(maps.size()));
		Arena arena = finalBuilder.newArena();
		arena.setGameMap(map);
		
		int i =0;
		for (Party p1: context.getForce1Paries()){
			PartialArena05 pp = (PartialArena05) p1;
			for (UserSlot userSlot:pp.getSlots()){
				if (userSlot.getUser()!=null){
					arena.getSlots().get(i).setUser(userSlot.getUser());
					Map<String, String> userExtra = arena.getSlots().get(i).getExtra();
					userExtra.putAll(userSlot.getExtra());
					userExtra.put("partyUuid", pp.getUuid().toString());
					userExtra.put("partyInt32Id", Integer.toString(pp.getInt32Id()));
					++i;	
				}
				
			}
		}
		
		i=m;
		for (Party p2: context.getForce2Paries()){
			PartialArena05 pp = (PartialArena05) p2;
			for (UserSlot userSlot:pp.getSlots()){
				if (userSlot.getUser()!=null){
					arena.getSlots().get(i).setUser(userSlot.getUser());
					Map<String, String> userExtra = arena.getSlots().get(i).getExtra();
					userExtra.putAll(userSlot.getExtra());
					userExtra.put("partyUuid", pp.getUuid().toString());
					userExtra.put("partyInt32Id", Integer.toString(pp.getInt32Id()));
					++i;
				}
			}
		}
		for(ArenaSlot slot:arena.getSlots()){
			if (slot.getUser()!=null){
				slot.getUser().setStatus(UserStatus.GAMING);
			}
		}
		
		User leader = null;
		for(ArenaSlot slot:arena.getSlots()){
			if (slot.getUser()!=null){
				leader = slot.getUser();
				break;
			}
		}
		assert leader != null;
		arena.getAttributes().put("hostID", String.valueOf(leader.getId()));
		return arena;
	}
	
	

	synchronized public Arena05 userCreateArena(User operator,
			String mode) {
		Event.assertUserAvailable(operator);
		Arena05 arena;
		DefaultArenaBuilder builder = (DefaultArenaBuilder) partyBuilder.copy();
		//builder.setGameMap(6);
		builder.setSlotNum(m, 0);
		arena = (Arena05)builder.newArena();
		arena.save();
		arena.addPlayer(operator);
		arena.setLeader(operator);
		arena.setMode(getMode());
		arena.userStatusEx = this.name;
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
	public ResGame.EventGet.Builder toProtobuf() {
		throw new NotFullySupportedException("");
	}
}

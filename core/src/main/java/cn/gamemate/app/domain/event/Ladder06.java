package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.response.ResGame;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.ArenaExtension;
import cn.gamemate.app.domain.arena.ArenaSlot;
import cn.gamemate.app.domain.arena.ArenaStatusChangedEvent;
import cn.gamemate.app.domain.arena.ArenaUserLeavedSignal;
import cn.gamemate.app.domain.arena.DefaultArenaBuilder;
import cn.gamemate.app.domain.arena.UserSlot;
import cn.gamemate.app.domain.arena.Arena.ArenaStatus;
import cn.gamemate.app.domain.arena.msg.ArenaStartMessage;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.party.DefaultParty;
import cn.gamemate.app.domain.party.LadderInvitationMessage;
import cn.gamemate.app.domain.party.PartyChangedSignal;
import cn.gamemate.app.domain.party.PartyClosedSignal;
import cn.gamemate.app.domain.party.PartyExtension;
import cn.gamemate.app.domain.party.PartyManager;
import cn.gamemate.app.domain.party.PartyMember;
import cn.gamemate.app.domain.party.PartyMemberUpdateMessage;
import cn.gamemate.app.domain.user.AlertMessage;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.User.UserStatus;
import cn.gamemate.app.domain.user.UserExtension;
import cn.gamemate.app.domain.user.UserRepository;
import cn.gamemate.app.domain.user.UserStatusUpdateMessage;

@Configurable
public class Ladder06 extends Ladder implements DomainModel{
	// for party and user
	
	protected GreedyMatcher matcher;
	
	@Autowired(required=true)
	protected PartyManager partyManager;
	
	@Autowired(required=true)
	protected UserRepository userRepository;
	
	private final Map<Integer, DefaultParty> singleUserIndex= new HashMap<Integer, DefaultParty>();
	Integer m;
	String mode;
	DefaultArenaBuilder finalBuilder;
	private Random random = new Random();
	private final int TICK_INTERVAL = 1000;
	private java.util.Timer timer;
	String name ;
	Game game;
	private PartyExtension partyExtension;
	/**
	 * @param matcher the matcher to set
	 */
	public void setMatcher(GreedyMatcher matcher) {
		this.matcher = matcher;
		matcher.addHandler(new MatcherHandler(){
			@Override
			public void onMatched(MatcherContext e) {
				Arena finalArena = makeGame(e);
				finalArena.save();
				finalArena.addExtension(new ArenaExtension(){
					@Override
					public void statusChanged(ArenaStatusChangedEvent e) {
						if (e.oldStatus == ArenaStatus.GAMING){
							Arena arena = e.getModel();
							for (ArenaSlot slot:arena.getSlots()){
								slot.getUser().setArenaId(null);
							}
							arena.close();
						}
					}
					
					@Override
					public void userLeaved(ArenaUserLeavedSignal e) {
						
					}
				});
				for (ArenaSlot slot:finalArena.getSlots()){
					slot.getUser().cacEventId(id);
					new UserStatusUpdateMessage(slot.getUser()).send();
				}
				new ArenaStartMessage(finalArena).send();
			}
		});
	}
	
	/**
	 * copy matched arenas into one new Arena 
	 * @param context 
	 */
	protected Arena makeGame(MatcherContext context){
		
		
		GameMap map = gameMaps.get(random.nextInt(gameMaps.size()));
		Arena arena = finalBuilder.newArena();
		arena.setGameMap(map);
		
		int i =0;
		for (Party p1: context.getForce1Paries()){
			DefaultParty pp = (DefaultParty) p1;
			for (UserSlot userSlot:pp.getMembers()){
				if (userSlot.getUser()!=null){
					arena.getSlots().get(i).setUser(userSlot.getUser());
					Map<String, String> userExtra = arena.getSlots().get(i).getExtra();
					userExtra.putAll(userSlot.getExtra());
					//userExtra.put("partyUuid", pp.getUuid().toString());
					//userExtra.put("partyInt32Id", Integer.toString(pp.getInt32Id()));
					++i;	
				}
				
			}
		}
		
		i=m;
		for (Party p2: context.getForce2Paries()){
			DefaultParty pp = (DefaultParty) p2;
			for (UserSlot userSlot:pp.getMembers()){
				if (userSlot.getUser()!=null){
					arena.getSlots().get(i).setUser(userSlot.getUser());
					Map<String, String> userExtra = arena.getSlots().get(i).getExtra();
					userExtra.putAll(userSlot.getExtra());
					//userExtra.put("partyUuid", pp.getUuid().toString());
					//userExtra.put("partyInt32Id", Integer.toString(pp.getInt32Id()));
					++i;
				}
			}
		}
		for(ArenaSlot slot:arena.getSlots()){
			if (slot.getUser()!=null){
				slot.getUser().setStatus(UserStatus.GAMING);
				slot.getUser().setArenaId(arena.getInt32Id());
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

	public void start(){
		userRepository.addExtension(new UserExtension(){
			@Override
			public void userDrop(User user) {
				cleanLadder(user);
			}
			@Override
			public void userLoggedOut(User user) {
				cleanLadder(user);
			}
			void cleanLadder(User user){
				Party party = singleUserIndex.remove(user.getId());
				if (party !=null){
					matcher.remove(party);
				}
			}
		});
		partyExtension = new PartyExtension(){
			@Override
			public void closed(PartyClosedSignal e) {
				matcher.remove(e.getModel());
				releaseParty(e.getModel());
			}
			@Override
			public void partyChanged(PartyChangedSignal e) {
				matcher.remove(e.getModel());
				releaseParty(e.getModel());
			}
		};
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
	
	
	@Override
	public Arena05 userCreateArena(User operator, String mode) {
		return null;
		
	}
	@Override
	synchronized 
	public void singleJoin(User operator, String mode, Map<String, String> leaderAttributes){
		Arena05.assertUserNotInArena(operator);
		acquireUser(operator);		
		try{
			DefaultParty party = new DefaultParty();
			PartyMember userSlot = party.addUser(operator, false);
			for (Entry<String, String> entry: leaderAttributes.entrySet()){
				userSlot.setAttribute(entry.getKey(), entry.getValue());
			}
			singleUserIndex.put(operator.getId(), party);
			matcher.add(party);
			new UserStatusUpdateMessage(operator).send();
		}
		catch(RuntimeException e){
			releaseUser(operator);
			throw e;
		}
		return;
	}
	
	
	
	@Override
	synchronized
	public void partyJoin(User operator, String mode, Map<String, String> leaderAttributes){
		DefaultParty party = partyManager.getParty(operator);
		//Lock party
		synchronized(party){
			party.assertPartyLeader(operator);
			for (PartyMember partyMember: party.getMembers()){
				if (partyMember.isOut())
					throw new DomainModelRuntimeException(partyMember.getUser().getName()+" is out");
				Arena05.assertUserNotInArena(partyMember.getUser());
			}
			acquireParty(party);
			PartyMember userSlot = party.getLeaderSlot();
			for (Entry<String, String> entry: leaderAttributes.entrySet()){
				userSlot.setAttribute(entry.getKey(), entry.getValue());
			}
			new LadderInvitationMessage(party, this).send();
			
		}
		
	}
	
		

	@Override	
	public void userLeave(User operator){
		DefaultParty party = partyManager.getParty(operator, false);
		if (party==null){
			remove(operator);
			new UserStatusUpdateMessage(operator).send();
		}	
		else{
			remove(party);
			List<Integer> receivers = new ArrayList<Integer>();
			for(PartyMember member:party.getMembers()){
				if (!member.getUser().equals(operator)){
					receivers.add(member.getUser().getId());
				}
			}
			new AlertMessage(receivers, operator.getUsername()+"取消了游戏", true).send();
			new PartyMemberUpdateMessage(party).send();
		}
		
	}
	@Override	
	public void remove(Object object){
		if (object instanceof User){
			
			User operator = (User)object;
			DefaultParty party;
			party = partyManager.getParty(operator, false);
			if (party == null){
				party = singleUserIndex.remove(operator.getId());
			}
			if (party != null){
				matcher.remove(party);
				party.removeExtension(partyExtension);
				releaseParty(party);
			}
		}
		else if (object instanceof DefaultParty){
			DefaultParty party = (DefaultParty)object;
			matcher.remove(party);
			party.removeExtension(partyExtension);
			releaseParty(party);
		}
		else{
			throw new RuntimeException("can not reach here");
		}
	}
	@Override
	public void add(Object object){
		if (object instanceof DefaultParty){
			DefaultParty party = (DefaultParty)object;
			party.addExtension(partyExtension);
			matcher.add(party);
		}
	}
	
	

	@Override
	public ResGame.EventGet.Builder toProtobuf() {
		ResGame.EventGet.Builder builder = ResGame.EventGet.newBuilder();
		builder.setName(name).setId(id);
		builder.setPhysicalGame(game.getPhysicalGame().toProtobuf());
		for (GameMap map : gameMaps){
			builder.addRequiredMaps(map.toProtobuf());
		}
		return builder;
	}

}

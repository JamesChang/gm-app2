package cn.gamemate.app.domain.arena;

import java.util.ArrayList;
import java.util.List;

import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.event.Arena05;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.game.PhysicalGame;

public class DefaultArenaBuilder implements ArenaBuilder, Cloneable{
	protected Integer homeSlotNum=0;
	protected Integer awaySlotNum=0;
	protected String name;
	protected GameMap map=null;
	protected Game game;
	protected String version;
	protected List<ArenaExtension> extensions = new ArrayList<ArenaExtension>();
	//protected PhysicalGame physicalGame;

	//TODO: using validators
	
	public DefaultArenaBuilder setSlotNum(Integer home, Integer away){
		this.homeSlotNum = home;
		this.awaySlotNum = away;
		return this;
	}
	
	public DefaultArenaBuilder bisectSlots(){
		//TODO: if map not set, delay the work to the time set map
		int max_map_users = this.map.getMaxUserCount();
		//TODO: assert max_map_users > 0 and is even
		homeSlotNum = max_map_users /2;
		awaySlotNum = max_map_users /2;
		return this;
	}
	
	public DefaultArenaBuilder setName(String name){
		this.name = name;
		return this;
	}
	
	public DefaultArenaBuilder setGameMap(GameMap map){
		this.map = map;
		return this;
	}
	
	
	public GameMap getGameMap() {
		return map;
	}

	public DefaultArenaBuilder setGame(Game game){
		this.game = game;
		return this;
	}
	
	public DefaultArenaBuilder setGameMap(Integer mapid){
		this.map = GameMap.findGameMap((long)mapid);
		//TODO check null
		return this;
	}
	
	public DefaultArenaBuilder setGameVersion(String version){
		this.version = version;
		return this;
	}
	
	public DefaultArenaBuilder addExtension(ArenaExtension x){
		this.extensions.add(x);
		return this;		
	}
	

	@Override
	public boolean isReady() {
		//TODO:
		return true;
	}
	
	protected Arena05 _newArena(){
		return new Arena05();
	}

	@Override
	public Arena05 newArena() {
		//TODO set class
		Arena05 arena = _newArena();
		if (this.name != null){
			arena.setName(this.name);
		}
		arena.gameMap = this.map;
		arena.game = this.game;
		arena.physicalGame = game.getPhysicalGame();
		
		//arena.physicalGame = this.
		ArenaForce force1 = arena.addForce("Team 1");
		for(int i =0;i<homeSlotNum;++i){
			ArenaSlot slot = new ArenaSlot(arena, null, force1, false,i, null);
			arena.slots.add(slot);
		}
		
		if (awaySlotNum > 0){
			ArenaForce force2 = new ArenaForce();
			force2.id = 1;
			force2.label = "Team 2";
			force2.arena = arena;
			arena.forces.add(force2);
			
			for(int i =homeSlotNum;i<homeSlotNum +awaySlotNum;++i){
				ArenaSlot slot = new ArenaSlot(arena, null, force2, false, i, null);
				arena.slots.add(slot);
			}
		}
		
		
		//user attr def
		arena.userAttrDefs.putAll(arena.game.getUserAttrs());
		
		//set extensions
		arena.addAllExtensions(extensions);
		
		return arena;
		
	}

	@Override
	public ArenaBuilder copy() {
		try {
			return (ArenaBuilder) super.clone();
		} catch (CloneNotSupportedException e) {
			// Can Not Happen
			throw new DomainModelRuntimeException("Can Not Happen", e);
		}
	}
	
	
}

package cn.gamemate.app.domain.arena;

import java.util.ArrayList;
import java.util.List;

import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.event.Arena05;
import cn.gamemate.app.domain.game.GameMap;



/**
 * @author jameszhang
 *
 *merge two same single-force-arena into one
 */
public class ArenaMerger implements ArenaBuilder, Cloneable{
	
	private Arena home;
	private Arena away;
	private String name=null;
	protected GameMap map=null;
	protected List<ArenaExtension> extensions = new ArrayList<ArenaExtension>();
	
	public ArenaMerger setName(String name){
		this.name = name;
		return this;
	}
	
	public ArenaMerger setArenas(Arena home, Arena away){
		this.home = home;
		this.away = away;
		return this;
	}
	

	public ArenaMerger addExtension(ArenaExtension x){
		this.extensions.add(x);
		return this;		
	}
	
	public ArenaMerger setGameMap(GameMap map){
		this.map = map;
		return this;
	}
	
	public ArenaMerger setGameMap(Integer mapid){
		this.map = GameMap.findGameMap((long)mapid);
		//TODO check null
		return this;
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}
	
	

	@Override
	public Arena newArena() {

		Arena arena =new Arena05();
		if (this.name != null){
			arena.setName(this.name);
		}
		arena.gameMap= map != null ? map : home.getGameMap();
		arena.game = home.getGame();
		arena.physicalGame = home.getGame().getPhysicalGame();
		arena.mode = home.mode;
		
		//TODO: move this to base class, or inner defaultArenaBuilder;
		ArenaForce force1 = new ArenaForce();
		force1.id = 0;
		force1.label = "Team 1";
		force1.arena = arena;
		arena.forces.add(force1);
		
		int homeSlotNum = home.slots.size();
		int awaySlotNum = away.slots.size();
		for(int i =0;i<homeSlotNum;++i){
			ArenaSlot slot = new ArenaSlot(arena, home.slots.get(i).getUser(), force1, false,i, home.slots.get(i).getExtra());
			arena.slots.add(slot);
		}
		
		ArenaForce force2 = new ArenaForce();
		force2.id = 1;
		force2.label = "Team 2";
		force2.arena = arena;
		arena.forces.add(force2);

		for(int i =homeSlotNum;i<homeSlotNum +awaySlotNum;++i){
			ArenaSlot slot = new ArenaSlot(arena, away.slots.get(i-homeSlotNum).getUser(), force2, false, i, away.slots.get(i-homeSlotNum).getExtra());
			arena.slots.add(slot);
		}
		
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

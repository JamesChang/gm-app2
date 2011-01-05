package cn.gamemate.app.domain.arena;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


// NOT thread safe.
public class BasicArenaRepository<T> {
	
	private Map<T, Arena> arenaMap = new ConcurrentHashMap<T, Arena>();
	
	public int getCurrentArena() {
		return arenaMap.size();
	}

	public Arena get(T uuid){
		return arenaMap.get(uuid);
	}
	
	public void add(T key, Arena arena){
		//if (!arenaMap.containsKey(key)){
			arenaMap.put(key, arena);
		//}
	}
	
	public Arena remove(Arena arena){
		return arenaMap.remove(arena.getUuid());
	}
	public Arena remove(T id){
		return arenaMap.remove(id);
	}
	
	
	
	
	
}

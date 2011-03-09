package cn.gamemate.app.domain.arena;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import cn.gamemate.app.domain.arena.Arena.ArenaStatus;


// NOT thread safe.
public class BasicArenaRepository<T> implements BasicArenaRepositoryMBean{
	
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

	@Override
	public String getStatsJson() {
		int open=0;
		int closed=0;
		int other=0;
		int gaming=0;
		
		for (Entry<T, Arena> entry: arenaMap.entrySet()){
			Arena a = entry.getValue();
			ArenaStatus status = a.getStatus();
			switch(status){
			case GAMING:
				gaming = 0;
				break;
			case OPEN:
				open ++;
				break;
			case CLOSED:
				open ++;
				break;
			default:
				other = 0;
				break;
			}
		}
		
		return new StringBuilder()
			.append("Arenas{")
			.append("total:").append(open+closed + other + gaming).append(", ")
			.append("gaming:").append(gaming).append(", ")
			.append("open:").append(open).append(", ")
			.append("closed:").append(closed).append(", ")
			.append("others:").append(other).append(", ")
			.append("}")
			.toString();
	}
	
	
	
	
	
}

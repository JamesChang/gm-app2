package cn.gamemate.app.domain.event;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gamemate.app.domain.ObjectNotFound;

public class EventCenter {
	private static Logger logger = LoggerFactory.getLogger(EventCenter.class);
	
	private Map<Integer, Ladder> ladders = new HashMap<Integer, Ladder>();
	private Map<Integer, ArenaListEvent05> events = new HashMap<Integer, ArenaListEvent05>();
	
	public EventCenter(){
		
		
	}
	
	public void addLadder(int id, Ladder ladder){
		ladders.put(id, ladder);
	}
	public void addEvent(int id, ArenaListEvent05 event){
		events.put(id, event);
	}
	
	
	public ArenaListEvent05 getEvent(int id){
		ArenaListEvent05 event = events.get(id);
		if(event != null)
			return event;
		throw new ObjectNotFound(ArenaListEvent05.class, id);
	}
	
	public Ladder getLadder(int id){
		Ladder ladder = ladders.get(id);
		if(ladder != null)
			return ladder;
		throw new ObjectNotFound(DummyLadder.class, id);
	}

}


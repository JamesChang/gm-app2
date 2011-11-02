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
	private Map<Integer, Event> events = new HashMap<Integer, Event>();
	
	public EventCenter(){
		
		
	}
	
	public void addLadder(int id, Ladder ladder){
		ladders.put(id, ladder);
	}
	public void addEvent(int id, Hall event){
		events.put(id, event);
	}
	
	
	public Event getEvent(int id){
		if (id > 100 && id < 1000){
			// Ladders
			Ladder ladder = ladders.get(id);
			if(ladder != null)
				return ladder;
			throw new ObjectNotFound(DummyLadder.class, id);
		}else{
			Event event = events.get(id);
			if(event == null){
				event = EntityEvent.findEntityEvent(id);
			}
			if(event != null){
				return event;
			}
			throw new ObjectNotFound(Event.class, id);			
		}
	}
	
}


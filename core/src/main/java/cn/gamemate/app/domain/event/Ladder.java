package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.User;

abstract public class Ladder extends Event{
	final List<GameMap> gameMaps = new ArrayList<GameMap>();
	protected String version;
	protected Game game;
	
	
	abstract public Arena05 userCreateArena(User operator, String mode);
	
	public void singleJoin(User operator, String mode, Map<String, String> leaderAttributes){
		
	}
	public void partyJoin(User operator, String mode, Map<String, String> leaderAttributes){
		
	}
	public void userLeave(User operator){
		
	}
	public void remove(Object object){
		
	}
	public void add(Object object){
		
	}
}

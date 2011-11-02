package cn.gamemate.app.domain.arena;

import cn.gamemate.app.domain.DomainModelEvent;
import cn.gamemate.app.domain.DomainModelExtension;

public class ArenaExtension extends DomainModelExtension{
	
	
	@Override
	public void handleEvent(DomainModelEvent e) {
		if (e instanceof ArenaUserLeavedSignal){
			userLeaved((ArenaUserLeavedSignal)e);
		 }
		else if (e instanceof ArenaClosedEvent) {
			 closed((ArenaClosedEvent)e);
		 }else if (e instanceof ArenaStatusChangedEvent){
			 statusChanged((ArenaStatusChangedEvent)e);
		 }
	}
	
	public void closed(ArenaClosedEvent e){
		
	}
	
	
	public void statusChanged(ArenaStatusChangedEvent e){
		
	}
	
	public void userLeaved(ArenaUserLeavedSignal e){
		
	}
}

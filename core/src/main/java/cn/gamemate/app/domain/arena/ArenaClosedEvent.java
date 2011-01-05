package cn.gamemate.app.domain.arena;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelEvent;

public class ArenaClosedEvent extends DomainModelEvent {

	public ArenaClosedEvent(DomainModel model) {
		super(model);
	}
	
	@Override
	public Arena getModel(){
		return (Arena)model;
	}
	
	
}

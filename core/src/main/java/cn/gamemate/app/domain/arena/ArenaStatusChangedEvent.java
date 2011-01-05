package cn.gamemate.app.domain.arena;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelEvent;
import cn.gamemate.app.domain.arena.Arena.ArenaStatus;

public class ArenaStatusChangedEvent extends DomainModelEvent{
	
	public final ArenaStatus oldStatus;

	public ArenaStatusChangedEvent(DomainModel model, ArenaStatus oldStatus) {
		super(model);
		this.oldStatus = oldStatus;
	}
	
	@Override
	public Arena getModel(){
		return (Arena)model;
	}
}

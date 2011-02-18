package cn.gamemate.app.domain.arena;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelEvent;
import cn.gamemate.app.domain.arena.Arena.ArenaStatus;

public class ArenaStatusChangedEvent extends DomainModelEvent{
	
	public final ArenaStatus oldStatus;
	public final Boolean oldPrivate;
	
	public ArenaStatusChangedEvent(DomainModel model, ArenaStatus oldStatus, Boolean oldPrivate) {
		super(model);
		this.oldStatus = oldStatus;
		this.oldPrivate = oldPrivate;
	}

	public ArenaStatusChangedEvent(DomainModel model, ArenaStatus oldStatus) {
		this(model, oldStatus, null);
	}
	public ArenaStatusChangedEvent(DomainModel model, boolean oldPrivate) {
		this(model, null, oldPrivate);
	}
	
	@Override
	public Arena getModel(){
		return (Arena)model;
	}
}

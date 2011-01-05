package cn.gamemate.app.domain.arena;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelEvent;
import cn.gamemate.app.domain.arena.Arena.ArenaStatus;
import cn.gamemate.app.domain.user.User;

public class ArenaUserLeavedSignal extends DomainModelEvent{
	User user;
	
	public ArenaUserLeavedSignal(DomainModel model, User user) {
		super(model);
		this.user = user;
	}
	
	@Override
	public Arena getModel(){
		return (Arena)model;
	}

}

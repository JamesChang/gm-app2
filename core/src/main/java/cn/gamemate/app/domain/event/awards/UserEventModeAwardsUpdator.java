package cn.gamemate.app.domain.event.awards;

import cn.gamemate.app.domain.NotFullySupportedException;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Battle;

public class UserEventModeAwardsUpdator implements AwardsUpdator{

	@Override
	public void update(Battle battle, BattleAwards battleAwards) {
		// TODO Auto-generated method stub
		throw new NotFullySupportedException("");
	}

	@Override
	public BattleAwards prepareData(Arena arena) {
		// TODO Auto-generated method stub
		throw new NotFullySupportedException("");
	}

	@Override
	public String getName() {
		return "user_event_mode";
	}

}

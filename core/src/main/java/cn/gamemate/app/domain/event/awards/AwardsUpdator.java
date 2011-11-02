package cn.gamemate.app.domain.event.awards;

import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.event.Event;

public interface AwardsUpdator {

	public void update(Battle battle, BattleAwards battleAwards);
	public BattleAwards prepareData(Arena arena);
	public String getName();
}

package cn.gamemate.app.domain.event.awards;

import java.util.Map.Entry;

import org.springframework.transaction.annotation.Transactional;

import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.event.Event;

public class UserAwardsUpdator implements AwardsUpdator {

	@Override
	@Transactional
	public void update(Battle battle, BattleAwards battleAwards) {

		battle.setUserAwards(battleAwards);
		battle.update();
		
		 for (Entry<Integer, BattlePlayerAwards> entry: battleAwards.entrySet()){
			 //TODO: update user data
		 }

	}

	@Override
	public BattleAwards prepareData(Arena arena) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "user";
	}

}

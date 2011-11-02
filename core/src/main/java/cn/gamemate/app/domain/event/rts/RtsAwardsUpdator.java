package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.event.awards.BattleAwards;

public interface RtsAwardsUpdator {
	void update(RtsAwardsCalculatorContext context);
	BattleAwards getOriginalData(RtsAwardsCalculatorContext context);

}

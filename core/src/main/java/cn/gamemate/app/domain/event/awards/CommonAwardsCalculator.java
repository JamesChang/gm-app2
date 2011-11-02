package cn.gamemate.app.domain.event.awards;

import java.util.Iterator;

public class CommonAwardsCalculator implements AwardsCalculator{
		
	@Override
	public void calculate(AwardsCalculatorContext context){
		BattleAwards awards = context.getAwards();
		
		for (Iterator<Integer> iter = context.getWinners();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).total += 1;
			awards.getOrCreate(i).win += 1;
		}
		for (Iterator<Integer> iter = context.getLossers();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).total += 1;
		}
	}

}

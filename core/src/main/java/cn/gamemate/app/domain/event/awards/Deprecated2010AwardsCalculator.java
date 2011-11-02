package cn.gamemate.app.domain.event.awards;

import java.util.Iterator;

public class Deprecated2010AwardsCalculator implements AwardsCalculator{
	
	@Override
	public void calculate(AwardsCalculatorContext context){
		BattleAwards awards = context.getAwards();
		
		for (Iterator<Integer> iter = context.getWinners();iter.hasNext();){
			Integer i = iter.next();
			
			awards.getOrCreate(i).gold += 70;
			awards.getOrCreate(i).activity += 100;
		}
		for (Iterator<Integer> iter = context.getLossers();iter.hasNext();){
			Integer i = iter.next();

			awards.getOrCreate(i).gold += 20;
			awards.getOrCreate(i).activity += 30;
		}

	}

}

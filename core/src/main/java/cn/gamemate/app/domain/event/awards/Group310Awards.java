package cn.gamemate.app.domain.event.awards;

import java.util.Iterator;

import cn.gamemate.app.domain.event.rts.RtsAwardsCalculatorContext;
import cn.gamemate.app.domain.event.rts.RtsEliminationMatch;

public class Group310Awards implements AwardsCalculator{
	@Override
	public void calculate(AwardsCalculatorContext context){
		BattleAwards awards = context.getAwards();
		
		//Tricks for draw
		if (context instanceof RtsAwardsCalculatorContext){
			RtsAwardsCalculatorContext ctx = (RtsAwardsCalculatorContext) context;
			
			RtsEliminationMatch match = ctx.getMatch();
			if(match.getHomeScore() == match.getAwayScore()){
				for (Iterator<Integer> iter = ctx.getPlayers();iter.hasNext();){
					Integer i = iter.next();
					awards.getOrCreate(i).rtsScore += 1;
					awards.getOrCreate(i).total += 1;
					awards.getOrCreate(i).draw += 1;
				}
			}
		}
		
		for (Iterator<Integer> iter = context.getWinners();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).rtsScore += 3;
			awards.getOrCreate(i).total += 1;
			awards.getOrCreate(i).win += 1;
		}
		
		for (Iterator<Integer> iter = context.getLossers();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).total += 1;
		}
	}
}

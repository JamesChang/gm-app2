package cn.gamemate.app.domain.event.rts;

import java.util.Iterator;

import cn.gamemate.app.domain.event.awards.AwardsCalculator;
import cn.gamemate.app.domain.event.awards.AwardsCalculatorContext;
import cn.gamemate.app.domain.event.awards.BattleAwards;

public class RtsScoreCalculator implements AwardsCalculator{

	@Override
	public void calculate(AwardsCalculatorContext context) {
		if (!(context instanceof RtsAwardsCalculatorContext)){ return;}
		RtsAwardsCalculatorContext ctx = (RtsAwardsCalculatorContext) context;
		
		BattleAwards awards = context.getAwards();
		
		RtsEliminationMatch match = ctx.getMatch();
		Integer round = match.getRound();
		if (round==null){ return;}
		
		/*
		 * 1st = 5
		 * 2nd = 3
		 * 3rd = 2
		 * 4-8rd = 1
		 */
		int winScore=0, lossScore=0;
		if (round.equals(2)){
			winScore = 5;
			lossScore = 3;
		}else if (round.equals(34)){
			winScore = 1;
			lossScore = 0;
		}else if (round <= 8 && round > 0){
			winScore = 0;
			lossScore = 1;
		}
		

		for (Iterator<Integer> iter = context.getWinners();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).rtsScore += winScore;
			
			if (round.equals(2)){
				awards.getOrCreate(i).first += 1;
			}else if (round.equals(34)){
				awards.getOrCreate(i).third += 1;
			}
		}
		
		for (Iterator<Integer> iter = context.getLossers();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).rtsScore += lossScore;
			
			if (round.equals(2)){
				awards.getOrCreate(i).second += 1;
			}
		}
		
	}
	
	

}

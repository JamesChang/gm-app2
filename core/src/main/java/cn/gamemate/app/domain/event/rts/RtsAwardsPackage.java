package cn.gamemate.app.domain.event.rts;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.event.awards.AwardsCalculatorContext;
import cn.gamemate.app.domain.event.awards.AwardsPackage;
import cn.gamemate.app.domain.event.awards.BattleAwards;

public class RtsAwardsPackage extends AwardsPackage{
	

	public void calculateAndUpdate(AwardsCalculatorContext context0){
		
		RtsAwardsCalculatorContext context =  (RtsAwardsCalculatorContext)context0;
		
		loadOriginalAwards(context);
		loadDeltaAwards(context);
		
		logger.debug("Original {} {} ", context.getOriginalAwards());
		
		
		calculate(context);
		
		BattleAwards awards = context.getAwards();
		
		logger.debug("Delta {} {}", awards);
		
		saveDeltaAwards(context);
		(( RtsAwardsUpdator)getUpdator()).update(context);
		
	}
	
	
	public BattleAwards prepareOriginalData(RtsAwardsCalculatorContext context){
		BattleAwards awards = ((RtsAwardsUpdator)getUpdator())
			.getOriginalData(context);
		if (awards != null){
			String value = awards.toJson();
			context.getMatch().setOriginalAwards(value);
		}
		return awards;
	}
	
	public BattleAwards saveDeltaAwards(RtsAwardsCalculatorContext context){
		BattleAwards awards = context.getAwards();
		if (awards != null){
			String value = awards.toJson();
			context.getMatch().setDeltaAwards(value);
		}
		return awards;
	}
	
	public BattleAwards loadOriginalAwards(RtsAwardsCalculatorContext context){
		String originalAwards = context.getMatch().getOriginalAwards();
		if (originalAwards!=null){
			try {
				BattleAwards battleAwards = BattleAwards.fromJson(originalAwards);
				context.setOriginalAwards(battleAwards);
				return battleAwards;
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		context.setOriginalAwards(null);
		return null;
		
	}
	

	public BattleAwards loadDeltaAwards(RtsAwardsCalculatorContext context){
		String deltaAwards = context.getMatch().getDeltaAwards();
		if (deltaAwards!=null){
			try {
				BattleAwards battleAwards = BattleAwards.fromJson(deltaAwards);
				context.setLastAwards(battleAwards);
				return battleAwards;
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		context.setLastAwards(null);
		return null;
		
	}

}

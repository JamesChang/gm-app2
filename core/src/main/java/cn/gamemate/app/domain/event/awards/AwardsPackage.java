package cn.gamemate.app.domain.event.awards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proto.util.Util.StringDictItem;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.event.dota.DotaRepInfoCommonAwardsCalculatorContext;

public class AwardsPackage {
	
	static protected Logger logger = LoggerFactory.getLogger(AwardsPackage.class);
	
	List<AwardsCalculator> calculators = new ArrayList<AwardsCalculator>();
	AwardsUpdator updator;
	
	//- Properties -----------------------------------
	public AwardsUpdator getUpdator() {
		return updator;
	}
	public void setUpdator(AwardsUpdator updator) {
		this.updator = updator;
	}
	public List<AwardsCalculator> getCalculators() {
		return calculators;
	}
	public void setCalculators(List<? extends AwardsCalculator> calculators) {
		this.calculators.addAll(calculators);
	}

	//- Public Methods -------------------------------- 
	
	public void calculate(AwardsCalculatorContext context){
		
		for(AwardsCalculator calculator:calculators){
			calculator.calculate(context);
		}
	}
	
	
	public void calculateAndUpdate(Battle battle, AwardsCalculatorContext context){
		// move to calculate
		context.setOriginalAwards(loadStoredAwards(battle));
		logger.debug("Original {} {} ", getArenaAttributeKey(), loadStoredAwards(battle));
		
		
		calculate(context);
		BattleAwards awards = context.getAwards();
		logger.debug("Delta {} {}", getArenaAttributeKey(), awards);
		updator.update(battle, awards);
	}
	
	private String getArenaAttributeKey(){
		return "_" + updator.getName() + "_origin_awards";
	}
	
	public BattleAwards prepareOriginalData(Arena arena){
		BattleAwards awards = updator.prepareData(arena);
		if (awards != null){
			
			String key = getArenaAttributeKey();
			String value = awards.toJson();
			arena.getAttributes().put(key, value);
		}
		return awards;
	}
	
	public BattleAwards loadStoredAwards(Battle battle){
		BattleAwards battleAwards;
		String key = getArenaAttributeKey();
		for ( StringDictItem  attrItem:battle.getArenaSnapshot().getAttributesList()){
			if (attrItem.getKey().equals(key)){
				String data = attrItem.getValue();
				try {
					battleAwards = BattleAwards.fromJson(data);
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
		}
		return null;
	}
	
	
}
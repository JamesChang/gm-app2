package cn.gamemate.app.domain.event.awards;

import java.util.Iterator;

abstract public class AwardsCalculatorContext {
	
	private BattleAwards awards;
	private BattleAwards originalAwards;
	private BattleAwards lastAwards;
	private BattleAwards finalAwards;
	
	//-- abstract methods -----------------------
	
	public abstract Iterator<Integer> getWinners();
	public abstract Iterator<Integer> getLossers();
	
	//-- constructors ----------------------------
	
	public AwardsCalculatorContext() {
		this.awards = new BattleAwards();
		this.lastAwards=new BattleAwards();
	}
	
	public AwardsCalculatorContext(BattleAwards awards){
		this.awards = awards;
		this.finalAwards = new BattleAwards();
	}
	
	public AwardsCalculatorContext(BattleAwards awards, BattleAwards originalAwards){
		this(awards);
		this.originalAwards = originalAwards;
	}
	
	//-- properties ----------------------------
	
	public BattleAwards getFinalAwards() {
		return finalAwards;
	}

	public void setFinalAwards(BattleAwards finalAwards) {
		this.finalAwards = finalAwards;
	}

	public void setAwards(BattleAwards awards) {
		this.awards = awards;
	}

	
	
	public BattleAwards getOriginalAwards() {
		return originalAwards;
	}

	public void setOriginalAwards(BattleAwards originalAwards) {
		this.originalAwards = originalAwards;
	}

	public BattleAwards getAwards() {
		return awards;
	}
	public BattleAwards getLastAwards() {
		return lastAwards;
	}
	public void setLastAwards(BattleAwards lastAwards) {
		this.lastAwards = lastAwards;
	}
	
	//-- methods ------------------------------
	
	//public boolean isModified(Integer userId, )
	//public Object getFinalValue(Integer userId, )

}

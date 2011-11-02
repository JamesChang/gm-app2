package cn.gamemate.app.domain.event.rts;

import java.util.Iterator;
import java.util.Map.Entry;

import cn.gamemate.app.domain.event.EntityEvent;
import cn.gamemate.app.domain.event.awards.BattleAwards;
import cn.gamemate.app.domain.event.awards.BattlePlayerAwards;
import cn.gamemate.app.domain.event.awards.UserEventModeAwardsUpdator;
import cn.gamemate.app.domain.event.awards.UserEventModeData;
import cn.gamemate.app.domain.event.awards.UserGameData;

public class RtsUserEventModeAwardsUpdator extends UserEventModeAwardsUpdator  implements RtsAwardsUpdator{
	
	 public void update(RtsAwardsCalculatorContext context){
		 
		 //load eventid from home
		 RtsEliminationMatch match = context.getMatch();
		 EntityEvent parent = match.getParent();
		 Integer eventId = match.getParent().getParentId();
		 if (eventId == null){ return;}
		 
		 // load mode from parent
		 String mode = parent.getMode();
		 if (mode == null){ return;}
		 
		 BattleAwards battleAwards = context.getAwards();
		 BattleAwards lastAwards = context.getLastAwards();
		 if (lastAwards!=null){
			 battleAwards.minus(lastAwards); 
		 }
		 
		 for (Entry<Integer, BattlePlayerAwards> entry: battleAwards.entrySet()){
			 UserEventModeData userEventModeData = UserEventModeData.find(entry.getKey(), 
					 eventId, mode);
			 if (userEventModeData == null){
				 userEventModeData = new UserEventModeData();
				 userEventModeData.setUserID(entry.getKey());
				 userEventModeData.setEventID(eventId);
				 userEventModeData.setMode(mode);
				 userEventModeData.persist();
			 }
			 updateUserEventData(userEventModeData, entry.getValue());
			 
		 }
	 }

	 protected void updateUserEventData(UserEventModeData userGameData, BattlePlayerAwards awards){
		 if (awards.power != 0.0){
			 if (awards.power_final != 0.0){
				 userGameData.power = (int) awards.power_final;
			 }
			 userGameData.power += awards.power;
		 }
		 if (awards.trueskill_mean != 0.0){
			 userGameData.trueskill_mean = awards.trueskill_mean_final;
		 }
		 if (awards.trueskill_sd != 0.0){
			 userGameData.trueskill_sd= awards.trueskill_sd_final;
		 }
		 if (awards.total != 0){
			 userGameData.total += awards.total;
		 }
		 if (awards.win != 0){
			 userGameData.win += awards.win;
		 }
		 if (awards.activity != 0.0){
			 userGameData.activity += awards.activity;
		 }
		 if (awards.gold != 0.0){
			 userGameData.gold += awards.gold;
		 }
		 if (awards.rtsScore!=0){
			 userGameData.rtsScore += awards.rtsScore;
		 }
		 if (awards.first!=0){
			 userGameData.first += awards.first;
		 }
		 if (awards.second!=0){
			 userGameData.second += awards.second;
		 }
		 if (awards.third!=0){
			 userGameData.third += awards.third;
		 }
		 
	 }
	 

	 public BattleAwards getOriginalData(RtsAwardsCalculatorContext context){
		 BattleAwards result = new BattleAwards();

		 //load eventid from home
		 RtsEliminationMatch match = context.getMatch();
		 EntityEvent parent = match.getParent();
		 Integer eventId = match.getParent().getParentId();
		 if (eventId == null){ return result;}
		 
		 // load mode from parent
		 String mode = parent.getMode();
		 
		 for (Iterator<Integer> iter=context.getPlayers();iter.hasNext();){
			 Integer userId = iter.next();
			 UserEventModeData userEventModeData = UserEventModeData.find(
						userId,eventId, mode);
			 if (userEventModeData != null){
				 result.put(userId, userEventModeData);
			 }
		 }
		 return result; 
	 }

}

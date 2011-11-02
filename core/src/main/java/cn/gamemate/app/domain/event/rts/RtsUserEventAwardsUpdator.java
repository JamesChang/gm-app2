package cn.gamemate.app.domain.event.rts;

import java.util.Iterator;
import java.util.Map.Entry;

import cn.gamemate.app.domain.event.EntityEvent;
import cn.gamemate.app.domain.event.awards.BattleAwards;
import cn.gamemate.app.domain.event.awards.BattlePlayerAwards;
import cn.gamemate.app.domain.event.awards.UserEventAwardsUpdator;
import cn.gamemate.app.domain.event.awards.UserEventData;
import cn.gamemate.app.domain.event.awards.UserGameAwardsUpdator;
import cn.gamemate.app.domain.event.awards.UserGameData;

public class RtsUserEventAwardsUpdator 
extends UserEventAwardsUpdator 
implements RtsAwardsUpdator{

	@Override
	public void update(RtsAwardsCalculatorContext context) {
		RtsEliminationMatch match = context.getMatch();
		 Integer eventId = match.getParentId();
		 BattleAwards battleAwards = context.getAwards();
		 BattleAwards lastAwards = context.getLastAwards();
		 if (lastAwards!=null){
			 battleAwards.minus(lastAwards); 
		 }
		 
		 for (Entry<Integer, BattlePlayerAwards> entry: battleAwards.entrySet()){
			 UserEventData data = UserEventData.findUserEventData(entry.getKey(), 
					 eventId);
			 if (data == null){
				 data = new UserEventData();
				 data.setUserID(entry.getKey());
				 data.setEventID(eventId);
				 data.persist();
			 }
			 updateUserEventData(data, entry.getValue());
			 
		 }
	}
	
	@Override
	 public BattleAwards getOriginalData(RtsAwardsCalculatorContext context){
		Integer eventId = context.getMatch().getId();
		 BattleAwards result = new BattleAwards();
		 for (Iterator<Integer> iter=context.getPlayers();iter.hasNext();){
			 Integer userId = iter.next();
			 UserEventData data = UserEventData.findUserEventData(
						userId,eventId);
			 if (data != null){
				 result.put(userId, data.toBattlePlayerAwards());
			 }
		 }
		 return result; 
	 }


}

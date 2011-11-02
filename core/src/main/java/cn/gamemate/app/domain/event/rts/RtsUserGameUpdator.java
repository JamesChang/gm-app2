package cn.gamemate.app.domain.event.rts;

import java.util.Iterator;
import java.util.Map.Entry;

import org.springframework.transaction.annotation.Transactional;

import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.event.awards.BattleAwards;
import cn.gamemate.app.domain.event.awards.BattlePlayerAwards;
import cn.gamemate.app.domain.event.awards.UserGameAwardsUpdator;
import cn.gamemate.app.domain.event.awards.UserGameData;

public class RtsUserGameUpdator extends UserGameAwardsUpdator implements RtsAwardsUpdator{
	
	 public void update(RtsAwardsCalculatorContext context){
		 
		 Integer gameId = context.getMatch().getGameId();
		 BattleAwards battleAwards = context.getAwards();
		 BattleAwards lastAwards = context.getLastAwards();
		 if (lastAwards!=null){
			 battleAwards.minus(lastAwards); 
		 }
		 
		 for (Entry<Integer, BattlePlayerAwards> entry: battleAwards.entrySet()){
			 UserGameData userGameData = UserGameData.findUserGameData(entry.getKey(), 
					 gameId);
			 if (userGameData == null){
				 userGameData = new UserGameData();
				 userGameData.setUserID(entry.getKey());
				 userGameData.setGameID(gameId);
				 userGameData.persist();
			 }
			 updateUserGameData(userGameData, entry.getValue());
			 
		 }
		 
	 }
	 
	 public BattleAwards getOriginalData(RtsAwardsCalculatorContext context){
		 Integer gameId = context.getMatch().getGameId();
		 BattleAwards result = new BattleAwards();
		 for (Iterator<Integer> iter=context.getPlayers();iter.hasNext();){
			 Integer userId = iter.next();
			 UserGameData userGameData = UserGameData.findUserGameData(
						userId,gameId);
			 if (userGameData != null){
				 result.put(userId, userGameData);
			 }
		 }
		 return result; 
	 }
	
}

package cn.gamemate.app.domain.event.awards;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.transaction.annotation.Transactional;

import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.ArenaSlot;
import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.event.Event;

public class UserGameAwardsUpdator implements AwardsUpdator{
	 @Override
	 @Transactional
	 public void update(Battle battle, BattleAwards battleAwards){
		 
		 battle.setUserGameAwards(battleAwards);
		 battle.update();
		 
		 for (Entry<Integer, BattlePlayerAwards> entry: battleAwards.entrySet()){
			 UserGameData userGameData = UserGameData.findUserGameData(entry.getKey(), 
					 battle.getLogicalGameId());
			 if (userGameData == null){
				 userGameData = new UserGameData();
				 userGameData.setUserID(entry.getKey());
				 userGameData.setGameID(battle.getLogicalGameId());
				 userGameData.persist();
			 }
			 updateUserGameData(userGameData, entry.getValue());
			 
		 }
		 
	 }
	 
	 protected void updateUserGameData(UserGameData userGameData, BattlePlayerAwards awards){
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
		 
	 }

	@Override
	public BattleAwards prepareData(Arena arena) {
		int gameId = arena.getGame().getId();
		BattleAwards result = new BattleAwards();
		List<ArenaSlot> slots = arena.getPlayerSlots();
		for (ArenaSlot slot: slots){
			if (slot.getUser()!=null){
				UserGameData userGameData = UserGameData.findUserGameData(
						slot.getUser().getId(),
						gameId);
				if (userGameData != null){
					result.put(slot.getUser().getId(), userGameData);
					
				}
			}
		}
		return result;
	}

	@Override
	public String getName() {
		return "user_game";
	}
}

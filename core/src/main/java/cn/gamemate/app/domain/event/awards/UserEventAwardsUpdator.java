package cn.gamemate.app.domain.event.awards;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.transaction.annotation.Transactional;

import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.ArenaSlot;
import cn.gamemate.app.domain.arena.Battle;

public class UserEventAwardsUpdator implements AwardsUpdator{
	@Override
	 @Transactional
	 public void update(Battle battle, BattleAwards battleAwards){
		 
		 battle.setUserEventAwards(battleAwards);
		 battle.update();
		 
		 for (Entry<Integer, BattlePlayerAwards> entry: battleAwards.entrySet()){
			 int eventId = Integer.parseInt(battle.getArenaSnapshot().getEventId());
			 UserEventData userEventData = UserEventData.findUserEventData(entry.getKey(), eventId);
			 if (userEventData == null){
				 userEventData = new UserEventData();
				 userEventData.setUserID(entry.getKey());
				 userEventData.setEventID(eventId);
				 userEventData.persist();
			 }
			 updateUserEventData(userEventData, entry.getValue());
			 
		 }
		 
	 }
	 
	 protected void updateUserEventData(UserEventData userEventData, BattlePlayerAwards awards){
		 if (awards.power != 0.0){
			 if (awards.power_final != 0.0){
				 userEventData.power = (int) awards.power_final;
			 }
			 userEventData.power += awards.power;
		 }
		 if (awards.trueskill_mean != 0.0){
			 userEventData.trueskill_mean = awards.trueskill_mean_final;
		 }
		 if (awards.trueskill_sd != 0.0){
			 userEventData.trueskill_sd= awards.trueskill_sd_final;
		 }
		 if (awards.total != 0){
			 userEventData.total += awards.total;
		 }
		 if (awards.win != 0){
			 userEventData.win += awards.win;
		 }
		 if (awards.activity != 0.0){
			 userEventData.activity += awards.activity;
		 }
		 if (awards.gold != 0.0){
			 userEventData.gold += awards.gold;
		 }
		 
		 if (awards.rtsScore != 0.0){
			 userEventData.rtsScore += awards.rtsScore;
		 }
		 if (awards.draw != 0.0){
			 userEventData.draw += awards.draw;
		 }
		 
	 }

	@Override
	public BattleAwards prepareData(Arena arena) {
		int gameId = arena.getGame().getId();
		BattleAwards result = new BattleAwards();
		List<ArenaSlot> slots = arena.getPlayerSlots();
		for (ArenaSlot slot: slots){
			if (slot.getUser()!=null){
				UserEventData userEventData = UserEventData.findUserEventData(
						slot.getUser().getId(),
						gameId);
				if (userEventData != null){
					result.put(slot.getUser().getId(), userEventData.toBattlePlayerAwards());
					
				}
			}
		}
		return result;
	}

	@Override
	public String getName() {
		return "user_event";
	}
}

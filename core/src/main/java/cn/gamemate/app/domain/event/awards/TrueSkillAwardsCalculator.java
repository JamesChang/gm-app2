package cn.gamemate.app.domain.event.awards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;

import cn.gamemate.skill.GameInfo;
import cn.gamemate.skill.Player;
import cn.gamemate.skill.Rating;
import cn.gamemate.skill.Team;
import cn.gamemate.skill.trueskill.AbstractTrueSkillCalculator;
import cn.gamemate.skill.trueskill.TwoTeamTrueSkillCalculator;


public class TrueSkillAwardsCalculator implements AwardsCalculator {
	
	private GameInfo gameInfo;

    public GameInfo getGameInfo() {
        return gameInfo;
    }
    
    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

	@Override
	public void calculate(AwardsCalculatorContext context) {
		
		List<Team> teams = new ArrayList<Team>();
		Map<Integer, Player<Integer>> allPlayers  = new HashMap<Integer, Player<Integer>>();
		BattleAwards originalAwards = context.getOriginalAwards();
		
		//TODO: originalAwards is null
		
		//create winner team
		Team winnerTeam = new Team();
		for (Iterator<Integer> iter = context.getWinners();iter.hasNext();){
			Integer i = iter.next();
			Player<Integer> player = new Player<Integer>(i);
			allPlayers.put(i, player);
			
			BattlePlayerAwards data = originalAwards.get(i);
			
			Rating rating;
			if (data != null && data.trueskill_mean != 0.0 && data.trueskill_sd != 0.0){
				rating = new Rating(data.trueskill_mean, data.trueskill_sd);
			}else{
				rating = gameInfo.getDefaultRating();
			}
			winnerTeam.put(player, rating);
			
		}
		teams.add(winnerTeam);
		//create losser team
		Team losserTeam = new Team();
		for (Iterator<Integer> iter = context.getLossers();iter.hasNext();){
			Integer i = iter.next();
			Player<Integer> player = new Player<Integer>(i);
			allPlayers.put(i, player);
			
			BattlePlayerAwards data = originalAwards.get(i);
			
			Rating rating;
			if (data != null && data.trueskill_mean != 0.0 && data.trueskill_sd != 0.0){
				rating = new Rating(data.trueskill_mean, data.trueskill_sd);
			}else{
				rating = gameInfo.getDefaultRating();
			}
			losserTeam.put(player, rating);
			
		}
		teams.add(losserTeam);
		
		//calculation
		AbstractTrueSkillCalculator calculator;
		calculator = new TwoTeamTrueSkillCalculator();
		Map<Player<?>, Rating> newRatings = calculator.calculateNewRating(gameInfo, teams, new int[]{1,2});
		
		//output
		BattleAwards awards = context.getAwards();
		for (Iterator<Integer> iter = context.getWinners();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).trueskill_mean = 
				newRatings.get(allPlayers.get(i)).getMean() - winnerTeam.get(allPlayers.get(i)).getMean();
			awards.getOrCreate(i).trueskill_mean_final = newRatings.get(allPlayers.get(i)).getMean();
			awards.getOrCreate(i).trueskill_sd = 
				newRatings.get(allPlayers.get(i)).getStandardDeviation() - winnerTeam.get(allPlayers.get(i)).getStandardDeviation();
			awards.getOrCreate(i).trueskill_sd_final = newRatings.get(allPlayers.get(i)).getStandardDeviation();
		}
		for (Iterator<Integer> iter = context.getLossers();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).trueskill_mean = 
				newRatings.get(allPlayers.get(i)).getMean() - losserTeam.get(allPlayers.get(i)).getMean();
			awards.getOrCreate(i).trueskill_mean_final = newRatings.get(allPlayers.get(i)).getMean();
			awards.getOrCreate(i).trueskill_sd = 
				newRatings.get(allPlayers.get(i)).getStandardDeviation() - losserTeam.get(allPlayers.get(i)).getStandardDeviation();
			awards.getOrCreate(i).trueskill_sd_final = newRatings.get(allPlayers.get(i)).getStandardDeviation();
		}
		
	}
	
	
	
	
}

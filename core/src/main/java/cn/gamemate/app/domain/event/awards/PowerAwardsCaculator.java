package cn.gamemate.app.domain.event.awards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javassist.runtime.Inner;

import cn.gamemate.skill.GameInfo;
import cn.gamemate.skill.Player;
import cn.gamemate.skill.Rating;
import cn.gamemate.skill.Team;
import cn.gamemate.skill.elo.EloRating;
import cn.gamemate.skill.elo.FideKFactor;
import cn.gamemate.skill.elo.KFactor;
import cn.gamemate.skill.elo.TwoTeamEloCalculator;
import cn.gamemate.skill.trueskill.AbstractTrueSkillCalculator;
import cn.gamemate.skill.trueskill.TwoTeamTrueSkillCalculator;

public class PowerAwardsCaculator implements AwardsCalculator{
	
	private GameInfo gameInfo;
	private KFactor kFactor = new KFactor(K);
	private static final int K = 40;
	private static final int initialRating = 1500;

    public KFactor getkFactor() {
		return kFactor;
	}

	public void setkFactor(KFactor kFactor) {
		this.kFactor = kFactor;
	}

	public GameInfo getGameInfo() {
        return gameInfo;
    }
    
    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }
    
    class InnerPowerCalculator extends TwoTeamEloCalculator{
    	
    	public InnerPowerCalculator() {
    		super(kFactor);
    	}

    	@Override
    	protected double GetPlayerWinProbability(GameInfo gameInfo,
    			double playerRating, double opponentRating) {
    		double ratingDifference = opponentRating - playerRating;

            return 1.0
                   /
                   (
                       1.0 + Math.pow(10.0, ratingDifference / 400)
                   );
    	}
    	
    	

    }

	@Override
	public void calculate(AwardsCalculatorContext context) {

		List<Team> teams = new ArrayList<Team>();
		Map<Integer, Player<Integer>> allPlayers  = new HashMap<Integer, Player<Integer>>();
		
		//create winner team
		Team winnerTeam = new Team();
		for (Iterator<Integer> iter = context.getWinners();iter.hasNext();){
			Integer i = iter.next();
			Player<Integer> player = new Player<Integer>(i);
			allPlayers.put(i, player);
			//TODO: get this from some place.
			UserGameData data = UserGameData.findUserGameData(i, 2);
			
			Rating rating;
			if (data.power != 0.0 ){
				rating = new EloRating(data.power);
			}else{
				rating = new EloRating(initialRating);
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
			//TODO: get this from some place.
			UserGameData data = UserGameData.findUserGameData(i, 2);
			
			Rating rating;
			if (data.power != 0.0 ){
				rating = new EloRating(data.power);
			}else{
				rating = new EloRating(initialRating);
			}
			losserTeam.put(player, rating);
			
		}
		teams.add(losserTeam);
		
		//calculation
		TwoTeamEloCalculator calculator;
		calculator = new InnerPowerCalculator();
		Map<Player<?>, Rating> newRatings = calculator.calculateNewRating(gameInfo, teams, new int[]{1,2});
		
		//output
		BattleAwards awards = context.getAwards();
		for (Iterator<Integer> iter = context.getWinners();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).power = 
				newRatings.get(allPlayers.get(i)).getMean() - winnerTeam.get(allPlayers.get(i)).getMean();
			awards.getOrCreate(i).power_final = newRatings.get(allPlayers.get(i)).getMean();
		}
		for (Iterator<Integer> iter = context.getLossers();iter.hasNext();){
			Integer i = iter.next();
			awards.getOrCreate(i).power = 
				newRatings.get(allPlayers.get(i)).getMean() - losserTeam.get(allPlayers.get(i)).getMean();
			awards.getOrCreate(i).power_final = newRatings.get(allPlayers.get(i)).getMean();
		}
		
	}

}

package cn.gamemate.app.domain.event.rts;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.collect.Lists;

import cn.gamemate.app.domain.event.awards.AwardsCalculatorContext;
import cn.gamemate.app.domain.user.User;

public class RtsAwardsCalculatorContext extends AwardsCalculatorContext{
	
	private RtsEliminationMatch match;
	
	public RtsEliminationMatch getMatch() {
		return match;
	}

	public void setMatch(RtsEliminationMatch match) {
		this.match = match;
	}

	public RtsAwardsCalculatorContext(RtsEliminationMatch match) {
		super();
		this.match = match;
	}

	@Override
	public Iterator<Integer> getWinners() {
		
		RtsEventForce force = match.getWinnerForce();
		if (force!=null){
			ArrayList<Integer> list = Lists.newArrayList();
			for (User user:force.getPlayers()){
				list.add(user.getId());
			}
			return list.iterator();
		}
		else{
			return new ArrayList<Integer>().iterator();
		}
		
	}
	
	@Override
	public Iterator<Integer> getLossers() {
		
		RtsEventForce force = match.getLosserForce();
		if (force!=null){
			ArrayList<Integer> list = Lists.newArrayList();
			for (User user:force.getPlayers()){
				list.add(user.getId());
			}
			return list.iterator();
		}
		else{
			return new ArrayList<Integer>().iterator();
		}
		
	}

	public Iterator<Integer> getPlayers() {
		ArrayList<Integer> list = Lists.newArrayList();
		if (match.getHome()!=null){
			for (User user:match.getHome().getPlayers()){
				list.add(user.getId());
			}
			
		}
		if (match.getAway()!=null){
			for (User user:match.getAway().getPlayers()){
				list.add(user.getId());
			}
			
		}
		return list.iterator();
		
	}

}

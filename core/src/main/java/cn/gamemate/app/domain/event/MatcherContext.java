package cn.gamemate.app.domain.event;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import cn.gamemate.app.domain.arena.UserSlot;

import cn.gamemate.app.domain.user.User;

public class MatcherContext {
	
	protected List<Party> force1Parties;
	protected List<Party> force2Parties;
	
	public List<Party> getForce1Paries() {
		return force1Parties;
	}
	
	/**
	 * @return the force2
	 */
	public List<Party> getForce2Paries() {
		return force2Parties;
	}
	

}

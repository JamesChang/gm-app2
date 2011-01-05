package cn.gamemate.app.domain.event;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import proto.response.ResGame;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.arena.UserSlot;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.party.DefaultParty;
import cn.gamemate.app.domain.party.PartyManager;
import cn.gamemate.app.domain.user.User;

abstract public class Event implements DomainModel{
	protected Integer id;
	
	public Integer getId() {
		return id;
	}
	
	protected String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	static public void assertUserAvailable(User user) {
		if (user.getEventId() != null){
			throw new DomainModelRuntimeException("target user is not availble");
		}
	}

	protected void acquireUser(User user){
		try{
			user.casEventId(id);
		}
		catch (DomainModelRuntimeException e){
			throw new DomainModelRuntimeException("target user is not availble");
		}
	}
	protected void releaseUser(User user){
		user.cacEventId(id);
	}

	protected void acquireParty(DefaultParty party){
		ArrayList<User> t = new ArrayList<User>(); 
		try {
			for (UserSlot slot : party.getMembers()) {
				acquireUser(slot.getUser());
				t.add(slot.getUser());
			}
		} catch (DomainModelRuntimeException e) {
			for (User tu: t){
				releaseUser(tu);
			}
			throw e;
		}
	}

	protected void releaseParty(DefaultParty party){
		for (UserSlot slot : party.getMembers()) {
			try{
				slot.getUser().cacEventId(id);
			}
			catch(DomainModelRuntimeException e){
				
			}
		}
	}
	@Override
	abstract public ResGame.EventGet.Builder toProtobuf();
}

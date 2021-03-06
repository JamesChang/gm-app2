package cn.gamemate.app.domain.event;

import java.util.ArrayList;

import proto.response.ResEvent;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.arena.UserSlot;
import cn.gamemate.app.domain.event.awards.AwardsPackage;
import cn.gamemate.app.domain.party.DefaultParty;
import cn.gamemate.app.domain.party.PartyMember;
import cn.gamemate.app.domain.user.User;

abstract public class Event implements DomainModel{
	protected Integer id;
	private Iterable<AwardsPackage> awardsPackages = new ArrayList<AwardsPackage>();


	public Iterable<AwardsPackage> getAwardsPackages() {
		return awardsPackages;
	}

	public void setAwardsPackage(Iterable<AwardsPackage> awardsPackages) {
		this.awardsPackages = awardsPackages;
	}
	
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

	public void assertAvailable(User user) {
		if (user.isInEvent()){
			throw new DomainModelRuntimeException("target user is has joined other event "+user.getEventId());
		}
	}
	public void assertAvailable(DefaultParty party){
		for (UserSlot slot : party.getMembers()) {
			if (slot.getUser().isInEvent()){
				throw new DomainModelRuntimeException("some user is not availble");
			}
		}
	}

	protected void acquireUser(User user){
		try{
			user.casEventId(0, id);
		}
		catch (DomainModelRuntimeException e){
			throw new DomainModelRuntimeException("target user is not availble");
		}
	}
	protected void releaseUser(User user){
		user.casEventId(id, 0);
	}

	protected void acquireParty(DefaultParty party){
		for (PartyMember partyMember : party.getMembers()) {
			if (partyMember.isWaited())
				throw new DomainModelRuntimeException(partyMember.getUser()
						.getName() + " is busy");
		}
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

	public void releaseParty(DefaultParty party){
		for (UserSlot slot : party.getMembers()) {
			try{
				slot.getUser().casEventId(id, 0);
			}
			catch(DomainModelRuntimeException e){
				
			}
		}
	}
	
	@Override
	public ResEvent.EventGet.Builder toProtobuf(){
		return toProtobuf(99);
	}
	
	abstract public ResEvent.EventGet.Builder toProtobuf(int verbose);
}

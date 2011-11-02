package cn.gamemate.app.domain.event.rts;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;

import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.Forbidden;
import cn.gamemate.app.domain.event.rts.RtsEliminationMatch.Status;
import cn.gamemate.app.domain.user.User;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@RooJavaBean
public class DoubleElimination 
	implements PlayerRegisterationSupport{
	
	//- Fields ------------------------------------------------
	
	@Basic
	@NotNull
    private RtsEventForceType eventForceType;
	
	@Basic
    @NotNull
	protected Integer gameId;
	
	@OneToMany
	@JoinColumn(name="parent_id")
	List<RtsEliminationMatch> children=Lists.newArrayList();
	
	@Basic
	java.util.Date creationDate = new Date();
	
	//- Helper Methods --------------------------------------------
	

	public RtsEventForce getPlayerEventForce(User user){
		if (this.eventForceType.equals(RtsEventForceType.TEAM)){
			RtsTeam team = RtsTeam.findRtsTeamByUserGame(user, gameId);
			if (team==null) {return null;}
			for (RtsEventForce userForce0:registeredForces){
				RtsTeamEventForce teamForce = (RtsTeamEventForce) userForce0;
				if (teamForce.getTeam()!=null && teamForce.getTeam().equals(team)){
					return teamForce;
				}
			}
			return null;
			
		}else if (this.eventForceType.equals(RtsEventForceType.USER)){
			if (user ==null){ return null;}
			for (RtsEventForce userForce0:registeredForces){
				RtsSingleUserEventForce userForce = (RtsSingleUserEventForce) userForce0;
				if (userForce.getUser()!=null && userForce.getUser().equals(user)){
					return userForce;
				}
			}
			return null;	
		}
		return null;
		
	}
	
	protected boolean canGenerateChildren(){
		for(RtsEliminationMatch subMatch:children){
			if (subMatch.getStatus().equals(Status.FINISHED)){
				return false;
			}
		}
		return true;
	}
	
	protected void deleteChildren(){
		for(RtsEliminationMatch subMatch:children){
			subMatch.remove();
		}
		this.children.clear();
	}
	
	//- Public Methods ---------------------------------------------
	
	public void generateChildren(){
		
		if (!canGenerateChildren()){
			throw new Forbidden("有些子赛事已经开始，禁止重新抽签");
		}
		//remove all children if they exist
		deleteChildren();
		
		int allCount = registeredForces.size();
		if (allCount == 0){
			return;
		}
		
		//shuffle registers
		Collections.shuffle(registeredForces, new Random());
		
		Map<String, RtsEliminationMatch> rounds = Maps.newHashMap();
		
		int upperCount = (int) Math.pow(2,Math.ceil(Math.log(allCount)/Math.log(2)));
		int order = 1;
		int roundPlayer=upperCount;
		
		while(roundPlayer > 0){
			
		}
		
	}
	
	
	
	//- Registration Support ----------------------------------------

	@Override
	public void playerRegister(User player, RtsTeam team) {
		RtsSingleUserEventForce force = new RtsSingleUserEventForce(player);
		if (!registeredForces.contains(force)){
			force.persist();
			registeredForces.add(force);
		}
	}


	//- PlayerRegisterationSupport ---------------------------------------------


	@Basic
	private Date expectedRegistrationStartTime;
	
	@Basic
	@Column(name="registeration_deadline")
	private Date expectedRegistrationEndTime;
	
	@Basic
	private Date expectedExtraCheckStartTime;
	
	@Basic
	private Date expectedExtraCheckEndTime;
	
	@Basic
	@NotNull
	private boolean autoExtraCheck=false;
	
	@Basic
	@Column(name="registeration_limit")
	private Integer registrationLimit=1000;
	
	@Basic
	@NotNull
	private boolean allowPlayerRegistrationAfterPrepared=false;
	
	@Basic
	@NotNull
	private boolean allowPlayerRegister=true;
	

	@ManyToMany
	@OrderColumn
	List<RtsEventForce> registeredForces=Lists.newArrayList();
	
	@ElementCollection
	List<String> requiredPlayerInfo=Lists.newArrayList();
	
	@Override
	public List<String> getRequiredPlayerInfo() {
		return requiredPlayerInfo;
	}
	

	@Override
	public void setRequiredPlayerInfo(List<String> requiredFields) {
		this.requiredPlayerInfo.clear();
		this.requiredPlayerInfo.addAll(requiredFields);
		
	}

	@Override
	public int getRegisteredForceCount() {
		return registeredForces.size();
	}

	@Override
	public List<RtsEventForce> getRegisteredForces() {
		return registeredForces;
	}

	@Override
	public void playerCheckIn(User player, RtsTeam team) {
		//check Time
		
		RtsEventForce force = getPlayerEventForce(player);
		if (force == null){
			throw new DomainModelRuntimeException("用户尚未报名");
		}
		force.setChecked(true);
	}

	@Override
	public Date getExpectedRegistrationStartTime() {
		if (expectedRegistrationStartTime == null){
			return this.creationDate;
		}else{
			return expectedRegistrationStartTime;
		}
	}

	@Override
	public void playerUnregister(User player, RtsTeam team) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void administratorUnregister(User operator, User player, RtsTeam team) {
		// TODO Auto-generated method stub
		
	}

	

}

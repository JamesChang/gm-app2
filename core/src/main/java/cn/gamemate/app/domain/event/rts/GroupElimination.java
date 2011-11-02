package cn.gamemate.app.domain.event.rts;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResEvent;
import proto.response.ResEvent.EventGet.Builder;

import com.google.common.collect.Lists;

import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.DomainModelView;
import cn.gamemate.app.domain.DomainModelViewSupport;
import cn.gamemate.app.domain.Forbidden;
import cn.gamemate.app.domain.event.EntityEvent;
import cn.gamemate.app.domain.event.Event;
import cn.gamemate.app.domain.event.EventCenter;
import cn.gamemate.app.domain.event.EventForce;
import cn.gamemate.app.domain.event.PermissionSupport;
import cn.gamemate.app.domain.event.rts.RtsEliminationMatch.Status;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.event.awards.UserEventData;

@RooJavaBean
@Entity
@RooEntity
@DiscriminatorValue("grp_elm")
public class GroupElimination extends EntityEvent
implements PlayerRegisterationSupport, DomainModelViewSupport, PermissionSupport,
ChildrenManagementSupport{

	
	@Autowired
	@Transient
	EventCenter eventCenter;
	
	@ManyToMany
	List<User> administrators=Lists.newArrayList();
	
	@Basic
	@NotNull
	private Integer defaultRequiredWin = 1;
	
	@Basic
	private Integer defaultMaxRound;
	
	@Basic
	@NotNull
    private RtsEventForceType eventForceType = RtsEventForceType.USER;
	
	@Basic
    @NotNull
	protected Integer gameId;
	
	@OneToMany
	@JoinColumn(name="parent_id")
	List<RoundRobin> children=Lists.newArrayList();
	
	@Basic
	@Column(name="rules")
	protected String rules;
	
	@Basic
	java.util.Date creationDate = new Date();
	
	@Basic
	@Column(name="expected_time")
	protected String expectedTime;
	
	//- Helper Methods --------------------------------------------
	
	
	protected boolean canGenerateChildren(){
		
		return true;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	protected void deleteChildren(){
		for(EntityEvent subMatch:children){
			subMatch.remove();
		}
		this.children.clear();
	}
	

	public Game getGame() {
		if (gameId != null) {
			return Game.findById(gameId);
		} else {
			return null;
		}
	}
	

	EntityEvent getParent(){
		if (this.parentId == null) return null;
		Event parent0 = eventCenter.getEvent(this.parentId);
		//RtsElimination parent = (RtsElimination) parent0;
		return (EntityEvent)parent0;
	}
	
	//- Public Methods ---------------------------------------------

	
	@Override
	public String getEventType() {
		return "GroupElimination";
	}
	
	//- Registration Support ----------------------------------------
	
	@Override
	public void playerRegister(User player, RtsTeam team) {

		if (this.eventForceType.equals(RtsEventForceType.USER)){
			if (player==null){
				throw new RuntimeException("user is null");
			}
			
			RtsSingleUserEventForce force = new RtsSingleUserEventForce(player);
			synchronized(this){
				if (!registeredForces.contains(force)){
					force.persist();
					registeredForces.add(force);
				}
			}
			
		}else if(this.eventForceType.equals(RtsEventForceType.TEAM)){
			if(team==null){
				throw new TeamRequiredException();
			}
			if (!player.equals(team.getLeader())){
				throw new Forbidden("没有权限：只有战队队长才能执行此操作");
			}
			
			RtsTeamEventForce force = new RtsTeamEventForce(team);
			synchronized(this){
				if (!registeredForces.contains(force)){
					force.persist();
					registeredForces.add(force);
				}
			}
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

	
//- PermissionSupport --------------------------------------------------
	
	@Override
	public void assertPermission(User user, String name) throws Forbidden {
		if(administrators.contains(user)){
			return;
		}
		EntityEvent parent0 = getParent();
		if (parent0 instanceof PermissionSupport){
			PermissionSupport parent = (PermissionSupport) parent0;
			parent.assertPermission(user, name);
		}else{
			throw new Forbidden();
		}
		
	}

	@Override
	public boolean checkPermission(User user, String name) {
		if(administrators.contains(user)){
			return true;
		}
		EntityEvent parent0 = getParent();
		if (parent0 instanceof PermissionSupport){
			PermissionSupport parent = (PermissionSupport) parent0;
			return parent.checkPermission(user, name);
		}else{
			return false;
		}
	}

	@Override
	public Iterable<User> findUsersByRole(String name) {
		EntityEvent parent0 = getParent();
		if (parent0 instanceof PermissionSupport){
			PermissionSupport parent = (PermissionSupport) parent0;
			return parent.findUsersByRole(name);
		}else{
			return administrators;
		}
	}
	
	//- DomainModelViewSupport ---------------------------------------
	
	@Override
	public Builder toProtobuf(int verbose) {
		ResEvent.EventGet.Builder builder = super.toProtobuf(verbose);
		if (gameId!=null){
			Game game = getGame();
			if (game!=null){
			proto.response.ResGame.LogicalGame.Builder builder2 = game.toProtobuf();
			builder.setLogicalGame(builder2);
			}
		}
		if (this.eventForceType!=null){
			builder.setEventForceType(eventForceType.toString());
		}
		
		if (verbose>2){
			
			for (RoundRobin sub:children){
				if (sub==null){continue;}
				com.google.protobuf.GeneratedMessage.Builder builder2 = sub.newView(null, "leaderboard").toProtobuf();
				builder.addChildren(
						(ResEvent.EventGet.Builder)(builder2)); 
			}
			
			//output registered forces
			for (EventForce force: registeredForces){
				builder.addRegisteredForces(force.toProtobuf());
			}
		}
		return builder;
		
	}
	
	public boolean playerCanRegister(){
		if (!isAllowPlayerRegister()){
			return false; 
		}
		Date registerationDeadline = getExpectedRegistrationEndTime();
		if (registerationDeadline != null && 
				registerationDeadline.compareTo(new java.util.Date())<0){
			return false;
		}
		Integer registerationLimit = getRegistrationLimit();
		if (registerationLimit != null && registeredForces.size() >= registerationLimit){
			return false;
		}
		if (children.size() > 0 && !isAllowPlayerRegistrationAfterPrepared()){
			return false;
		}
		return true;
	}
	
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

	@Override
	public DomainModelView newView(User operator, String viewName) {
		if (viewName.equals("default")){
			return newDefaultView(operator);
		}else {
			throw new RuntimeException("can not reach here");
		}
	}
	

	@Override
	public DomainModelView newView(User operator) {
		return newView(operator,"default");
	}
	

	
	private DomainModelView newDefaultView(User operator) {
		
		Builder builder = toProtobuf();
		RtsEventForce force = this.getPlayerEventForce(operator);
		builder.setHasRegistered(force != null);
		builder.setViewer(operator.getId());
		builder.setAllowRegisteration(playerCanRegister());
		
		//cn.gamemate.app.domain.event.rts.RoundRobin.LeaderBoard leaderboad = getLeaderboad(null);
		//builder.setCa078LeaderBoard(leaderboad.toProtobuf());
		/*if (expectedTime !=null){
			builder.setExpectedTime(expectedTime);
		}*/
		return new DomainModelView(builder);
	}
	
	//- Grouping Support -------------------------------------------------
	
	public RoundRobin newChildGroup(User operator, String groupName){
		
		//TODO: check status
		
		//TODO: groupName should be Null
		
		//check name. should be unique.
		for (RoundRobin g:children){
			if (g.getName().equals(groupName)){
				throw new DomainModelRuntimeException("小组重复名字");
			}
		}
		
		//check group count
		RoundRobin newGroup = new RoundRobin();
		newGroup.setGameId(this.gameId);
		newGroup.setName(groupName);
		newGroup.setDefaultMaxRound(this.defaultMaxRound);
		newGroup.setDefaultRequiredWin(this.defaultRequiredWin);
		newGroup.setEventForceType(this.eventForceType);
		newGroup.persist();
		children.add(newGroup);
		
		
		
		return newGroup;
	}
	
	public void deleteChildGroup(User operator, String groupName){
		
		//TODO: check status
		
		//TODO: groupName should be Null
		
		//check name. should be unique.
		RoundRobin targetGroup=null;
		for (RoundRobin g:children){
			if (g.getName().equals(groupName)){
				targetGroup = g;
				break;
			}
		}
		if (targetGroup!=null){
			targetGroup.deleteChildren();
			targetGroup.remove();
			children.remove(targetGroup);
		}
		
	}
	
	private RtsEventForce findRegisteredForce(Long forceId){
		for(RtsEventForce force:registeredForces){
			if (force.getId().equals(forceId)){
				return force;
			}
		}
		return null;
	}
	
	private RoundRobin findGroup(String groupName){
		for(RoundRobin rr:children){
			if (rr.getName().equals(groupName)){
				return rr;
			}
		}
		return null;
	}
	
	public void moveForceToGroup(User operator, Long forceId, String groupName){
		
		//TODO: check status
		
		//TODO: groupName should be Null
		
		RtsEventForce force = findRegisteredForce(forceId);
		RoundRobin targetGroup=null;
		if (force==null){return;}
		for(RoundRobin rr:children){
			if (rr.registeredForces.contains(force)){
				targetGroup = rr;
				break;
			}
		}
		if (targetGroup!=null){
			targetGroup.registeredForces.remove(force);
		}
		RoundRobin group = findGroup(groupName);
		if (group==null){return;}
		group.registeredForces.add(force);
		
	}
	
	//- ChildrenManagementSupport ---------------------------------------------

	@Override
	public void generateChildren() {
		for (RoundRobin rr:children){
			rr.generateChildren();
		}
		
	}

	@Override
	public void userGenerateChildren(User operator) {
		assertPermission(operator, null);
		generateChildren();
        //Trick
		for(RtsEventForce force:registeredForces){
            for (User user:force.getPlayers()){
            Integer userId = user.getId();
            Integer eventId = this.getId();
        
            UserEventData data = UserEventData.findUserEventData(
                             userId,
                             eventId);
            if (data == null){
            
                data = new UserEventData();
                data.setUserID(userId);
                data.setEventID(eventId);
                data.persist();
            }
            }
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
		/*
		RtsEventForce force = getPlayerEventForce(player);
		if (force == null){
			throw new DomainModelRuntimeException("用户尚未报名");
		}
		force.setChecked(true);*/
	}

	@Override
	public Date getExpectedRegistrationStartTime() {
		if (expectedRegistrationStartTime == null){
			return this.creationDate;
		}else{
			return expectedRegistrationStartTime;
		}
	}

	
}

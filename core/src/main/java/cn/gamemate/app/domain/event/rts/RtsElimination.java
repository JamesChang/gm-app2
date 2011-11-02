package cn.gamemate.app.domain.event.rts;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeSet;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResEvent;
import proto.response.ResEvent.EventGet.Builder;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.DomainModelView;
import cn.gamemate.app.domain.DomainModelViewSupport;
import cn.gamemate.app.domain.Forbidden;
import cn.gamemate.app.domain.NotFullySupportedException;
import cn.gamemate.app.domain.Team;
import cn.gamemate.app.domain.event.EntityEvent;
import cn.gamemate.app.domain.event.EventForce;
import cn.gamemate.app.domain.event.PermissionSupport;
import cn.gamemate.app.domain.event.rts.RtsEliminationMatch.Status;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.User;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@RooJavaBean
@Entity
@RooEntity
@DiscriminatorValue("rts_elm")
public class RtsElimination extends EntityEvent implements
		PlayerRegisterationSupport, DomainModelViewSupport, PermissionSupport,
		ChildrenManagementSupport{

	public String getShortDescription(){
		if (status.equals(RtsEliminationMatch.Status.FINISHED)){
			return "已结束";
		}
		
		if (this.playerCanRegister()){
			return "正在报名";
		}else{
			return "正在进行";
		}
	}
	
	
	
	
	@Override
	public String getEventType() {
		return "Elimination";
	}

	@Override
	public Builder toProtobuf(int verbose) {
		ResEvent.EventGet.Builder builder = super.toProtobuf(verbose);
		if (getExpectedRegistrationEndTime()  != null){
			builder.setRegisterationDeadline(getExpectedRegistrationEndTime().getTime()/1000);
		}
		builder.setAllowRegisteration(this.playerCanRegister());
		builder.setAllowFight(playerCanFight());
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

		if (expectedTime !=null){
			builder.setExpectedTime(expectedTime);
		}
		if (getRegistrationLimit() !=  null){
			builder.setRegisterationLimit(this.getRegistrationLimit());
		}
		builder.setText(getShortDescription());
		if (verbose>1){
			//output administrators
			for (User u : administrators){
				builder.addAdministrators(u.toProtobuf());
			}
			if (defaultPlayGround!=null){
				builder.setPlayGround(defaultPlayGround.getName());
			}
			builder.setRequiredWin(defaultRequiredWin);
			if (rules != null){
				builder.setRules(rules);
			}
		}
		
		if (verbose>2){
			//output rounds
			Map<Integer, ResEvent.EliminationRound.Builder> rounds = Maps.newLinkedHashMap();
			
			
			List<RtsEliminationMatch> copy_children=Lists.newArrayList(children);
			Collections.sort(copy_children, new Comparator<RtsEliminationMatch>() {

				@Override
				public int compare(RtsEliminationMatch o1,
						RtsEliminationMatch o2) {
					return o1.getChildOrder() - o2.getChildOrder();
				}
			});
			
			
			for (RtsEliminationMatch match:copy_children){
				Integer r = match.getRound();
				ResEvent.EliminationRound.Builder round;
				round = rounds.get(r);
				if (round == null){
					round = ResEvent.EliminationRound.newBuilder();
					round.setRo(r);
					rounds.put(r, round);
				}
				round.addEvents(match.toProtobuf(1));
			}
			
			//add round 0 first
			if (rounds.get(0) != null){
				proto.response.ResEvent.EliminationRound.Builder builder2 = rounds.get(0);
				int c = builder2.getEventsCount();
				int max=0;
				for (Integer k :rounds.keySet()){
					if (k>max && !k.equals(34)){
						max = k;
					}
				}
				for (int i =0;i<max-c;i++){
					proto.response.ResEvent.EventGet.Builder builder3 = proto.response.ResEvent.EventGet.newBuilder();
					builder2.addEvents(builder3);
				}
				builder.addRounds(builder2);
				rounds.remove(0);
			}
			
			//get final and 34
			ResEvent.EliminationRound.Builder finalRound = null;
			ResEvent.EliminationRound.Builder final34Round = null;
			if (rounds.get(2) != null){
				finalRound= rounds.get(2);
				rounds.remove(2);
			}
			if (rounds.get(34) != null){
				final34Round= rounds.get(34);
				rounds.remove(34);
			}
			
			//add other rounds
			for(Entry<Integer, proto.response.ResEvent.EliminationRound.Builder> entry
					:rounds.entrySet()){
				builder.addRounds(entry.getValue());
			}
			
			//add final and 34
			if (final34Round!=null){builder.addRounds(final34Round);}
			if (finalRound!=null){builder.addRounds(finalRound);}
			
			//output registered forces
			for (EventForce force: registeredForces){
				builder.addRegisteredForces(force.toProtobuf());
			}
			
			//output leader board
			RtsEventForce place1=null;
			RtsEventForce place2=null;
			RtsEventForce place3=null;
			for (RtsEventForce force: registeredForces){
				if (force.getBestRound()!=null && force.getBestRound().equals(1)){
					place1 = force;
				}else if (force.getBestRound()!=null && force.getBestRound().equals(2)){
					place2 = force;
				}else if (force.getBestRound()!=null && force.getBestRound().equals(3)){
					place3 = force;
				}
			}
			if (place1 != null && place2 != null & place3 != null){
				builder.addLeaderboard(place1.toProtobuf());
				builder.addLeaderboard(place2.toProtobuf());
				builder.addLeaderboard(place3.toProtobuf());
			}
			
			
		}
		
		return builder;
	}
	
	public void attachToPb(ResEvent.EventGet.Builder builder, User user){
		if (user ==null) {return;}
		RtsEventForce force = this.getPlayerEventForce(user);
		builder.setHasRegistered(force != null);
		builder.setViewer(user.getId());
		if (force==null){return;}
		if (force.getNextMatchId()!=null){
			RtsEliminationMatch match = (RtsEliminationMatch) RtsEliminationMatch.findEliminationMatch(force.getNextMatchId());
			if (match!=null){
				builder.setMyNextMatch(match.toProtobuf(1));
			}
		}
		if (force.getBestRound()!=null){
			builder.setMyBestRound(force.getBestRound());
		}
		if (isAutoExtraCheck()){
			builder.setCheckInRequired(!force.isChecked());
		
			if (!force.isChecked()){
				Date startTime = getExpectedExtraCheckStartTime();
				if (startTime!=null){
					builder.setCheckInStart(startTime.getTime()/1000);
				}
				Date endTime = getExpectedExtraCheckEndTime();
				if (endTime!=null){
					builder.setCheckInEnd(endTime.getTime()/1000);
				}
			}
		}
	}
	
	@Basic
	java.util.Date creationDate = new Date();
    
    @Basic
    @NotNull
	protected Integer gameId;
    
	@ManyToOne
	protected GameMap map;
	
	
	@OneToMany
	@JoinColumn(name="parent_id")
	List<RtsEliminationMatch> children=Lists.newArrayList();
	
	@Basic
	@NotNull
	RtsEliminationMatch.Status status = RtsEliminationMatch.Status.NEW;
	
	//@Embedded
	//RtsEliminationMatchBuilder defaultBuilder;
	
	@Basic
	@NotNull
	private Integer defaultRequiredWin = 1;
	
	protected RtsLocation defaultPlayGround;
	
	@Basic
	@NotNull
    private RtsEventForceType eventForceType = RtsEventForceType.USER;
	
	
	@Basic
	@Column(name="expected_time")
	protected String expectedTime;
	
	@Basic
	@Column(name="rules")
	protected String rules;
	
	
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
	
	public boolean playerCanUnregister(){
		Date registerationDeadline = getExpectedRegistrationEndTime();
		if (registerationDeadline != null && 
				registerationDeadline.compareTo(new java.util.Date())<0){
			return false;
		}
		if (children.size() > 0){
			return false;
		}
		return true;
	}
	

	public boolean playerCanFight(){
		return children.size() > 0;
	}
	
	public boolean isAdministrator(User user){
		return administrators.contains(user);
	}
	
	@ManyToMany
	List<User> administrators=Lists.newArrayList();
	

	public Game getGame() {
		if (gameId != null) {
			return Game.findById(gameId);
		} else {
			return null;
		}
	}
	
	@Override
	public void userGenerateChildren(User operator){
		assertPermission(operator, null);
		generateChildren();
		
	}
	
	@Override
	public void generateChildren(){
		
		//remove all children if they exist
		//TODO: check status
		if (children.size()> 0){
			for(RtsEliminationMatch match:children){
				match.remove();
			}
			children.clear();
		}
		
		int allCount = registeredForces.size();
		if (allCount == 0){
			return;
		}
		
		//shuffle registers
		Collections.shuffle(registeredForces, new Random());
		
		
		int qualifierCount = (int) Math.pow(2,Math.floor(Math.log(allCount)/Math.log(2)));
		int order = 1;
		
		//generate qualifying matches
		int qualifyingMatchCount = allCount - qualifierCount;
		for(int i=0;i<qualifyingMatchCount;i++){
			RtsEliminationMatch match = new RtsEliminationMatch();
			match.setName("Qualifying Match of " + this.getName());
			match.setRequiredWin(defaultRequiredWin);
			match.round = 0;
			match.setGameId(this.gameId);
			if (this.map != null){
				match.setMap(this.map);
			}
			match.updateStatus();
			match.setChildOrder(order++);
			if (this.defaultPlayGround != null){
				match.setPlayGround(this.defaultPlayGround);
			}
			if (this.eventForceType!=null){match.setEventForceType(this.eventForceType);}
				
			match.persist();
			children.add(match);
			
		}
		

		
		
		
		
		
		//generate regular matches
		int round = qualifierCount;
		int iLastRound = 0;
		int iThisRound = children.size();
		while(round > 1){
			
			int thisRoundCount = round/2;
			for (int i =0;i<thisRoundCount; i++){
				
				//create match
				RtsEliminationMatch match = new RtsEliminationMatch();
				match.setName("Round Of "+round + ", " +this.getName());
				match.setRequiredWin(defaultRequiredWin);
				match.round = round;
				match.setGameId(this.gameId);
				if (this.map != null){
					match.setMap(this.map);
				}
				match.updateStatus();
				match.setChildOrder(order++);
				if (this.defaultPlayGround != null){
					match.setPlayGround(this.defaultPlayGround);
				}
				if (this.eventForceType!=null){match.setEventForceType(this.eventForceType);}
				match.persist();
				children.add(match);
				
				//construct link
				if (iLastRound <iThisRound){
					children.get(iLastRound).setNextWinnerMatchId(match.getId());
					match.setLastLeftMatchId(children.get(iLastRound).getId());
					iLastRound +=1;
					if (iLastRound <iThisRound){
						children.get(iLastRound).setNextWinnerMatchId(match.getId());
						match.setLastRightMatchId(children.get(iLastRound).getId());
						iLastRound += 1;
					}
				}
			}
			
			round = round /2;
			iLastRound = iThisRound;
			iThisRound = children.size();
		}
		
		//put player in
		Iterator<RtsEventForce> iterator = registeredForces.iterator();
		for(RtsEliminationMatch m:children){
			if (m.getLastLeftMatchId() == null && iterator.hasNext()){
				RtsEventForce force = iterator.next();
				m.setHome(force);
				force.setNextMatchId(m.getId());
				force.setBestRound(m.getRound());
				m.updateStatus();
			}
			if (m.getLastRightMatchId() == null && iterator.hasNext()){
				RtsEventForce force = iterator.next();
				m.setAway(force);
				force.setNextMatchId(m.getId());
				force.setBestRound(m.getRound());
				m.updateStatus();
			}
		}
		
		//generate 3rd/4th round
		if (children.size()>=3){
			int iSemifinalRound1 = children.size()-3;
			int iSemifinalRound2 = children.size()-2;
			
			
			RtsEliminationMatch match = new RtsEliminationMatch();
			match.setName("Round Of "+34 + ", " +this.getName());
			match.setRequiredWin(defaultRequiredWin);
			match.round = 34;
			match.setGameId(this.gameId);
			if (this.map != null){
				match.setMap(this.map);
			}
			match.updateStatus();
			match.setChildOrder(order++);
			if (this.defaultPlayGround != null){
				match.setPlayGround(this.defaultPlayGround);
			}
			if (this.eventForceType!=null){match.setEventForceType(this.eventForceType);}
			match.persist();
			
			//插入到倒数第二个
			children.add(children.size()-1, match);
			
			children.get(iSemifinalRound1).setNextLosserMatchId(match.getId());
			match.setLastLeftMatchId(children.get(iSemifinalRound1).getId());
			children.get(iSemifinalRound2).setNextLosserMatchId(match.getId());
			match.setLastRightMatchId(children.get(iSemifinalRound2).getId());
		}
		
		
	}
	

	@Override
	public void administratorUnregister(User operator, User player, RtsTeam team) {
		if (! isAdministrator(operator)){
			throw new Forbidden();
		}
		unregister(player, team);
		
		
	}
	
	
	@Override
	public void playerUnregister(User player, RtsTeam team){
		
		if (!playerCanUnregister()){
			throw new Forbidden();
		}
		
		if (team != null && this.eventForceType.equals(RtsEventForceType.TEAM)){
			if (!player.equals(team.getLeader())){
				throw new Forbidden("没有权限：只有战队队长才能执行此操作");
			}
		}
		
		unregister(player, team);
	}
	
	protected void unregister(RtsEventForce force){
		if (force == null){return;}
		force.setQuit(true);
		registeredForces.remove(force);	 
		/*
		RtsSingleUserEventForce userForce = new RtsSingleUserEventForce(player);
		registeredForces.remove(userForce);
		
		if (team != null && this.eventForceType.equals(RtsEventForceType.TEAM)){
		
			RtsTeamEventForce teamForce = new RtsTeamEventForce(team);
			registeredForces.remove(teamForce);	 
		}*/
		
		//search all children, mark this force
		/*for (RtsEliminationMatch match: children){
			if (force.equals(match.getHome()) || force.equals(match.getAway())){
				match.forceQuit(force);
			}
		}*/
	}

	protected void unregister(User player, RtsTeam team) {
		RtsEventForce force = getPlayerEventForce(player);
		unregister(force);
		if (team != null && this.eventForceType.equals(RtsEventForceType.TEAM)){
			
			RtsTeamEventForce teamForce = new RtsTeamEventForce(team);
			registeredForces.remove(teamForce);	 
		}
		
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
	
	public static List<RtsElimination> getPlayerRegisteredEvents(User player, RtsTeam team){
		TypedQuery<RtsElimination> query = entityManager().createQuery(
				"SELECT event FROM RtsElimination event JOIN fetch event.registeredForces force WHERE force.user = :user",
				
				RtsElimination.class);
		query.setParameter("user", player);
		List<RtsElimination> resultList = query.getResultList();
		if (team != null){
			TypedQuery<RtsElimination> query2 = entityManager().createQuery(
				"SELECT event FROM RtsElimination event JOIN fetch event.registeredForces force WHERE force.team = :team",
				
				RtsElimination.class);
			query2.setParameter("team", team);
			List<RtsElimination> resultList2 = query.getResultList();
			resultList.addAll(resultList2);
		}
		
		//add admins
		TypedQuery<RtsElimination> query3 = entityManager().createQuery(
				"SELECT event FROM RtsElimination event JOIN event.administrators admin " +
				"WHERE admin = :user", RtsElimination.class);
		query3.setParameter("user", player);
		List<RtsElimination> resultList3 = query3.getResultList();
		HashSet<RtsElimination> set = Sets.newHashSet();
		set.addAll(resultList);
		set.addAll(resultList3);
		return Lists.newArrayList(set.iterator());
	}
	
	public static List<RtsElimination> getTeamRegisteredEvents(RtsTeam team){
		TypedQuery<RtsElimination> query = entityManager().createQuery(
				"SELECT event FROM RtsElimination event JOIN fetch event.registeredForces force WHERE force.team = :team",
				
				RtsElimination.class);
		query.setParameter("team", team);
		List<RtsElimination> resultList = query.getResultList();
		return resultList;
	}

	public void batchMutateChildren(User operator,
			Integer round, Integer requiredWin, String expectedTime){
		
		if (!isAdministrator(operator)){
			throw new Forbidden();
		}
		
		List<RtsEliminationMatch> events = Lists.newArrayList();
		for (RtsEliminationMatch event:children){
			if (event.getRound().equals(round)){
				events.add(event);
			}
		}
		
		if (requiredWin != null){
			for (RtsEliminationMatch event:events){
				event.setRequiredWin(requiredWin);
			}
		}
		if (expectedTime != null){
			for (RtsEliminationMatch event:events){
				event.setExpectedTime(expectedTime);
			}
		}
		
		
	}
	
	public void addAdministrator(User operator, User user){

		if (!isAdministrator(operator)){
			throw new Forbidden();
		}
		
		if (!administrators.contains(user)){
			administrators.add(user);
		}
	}
	public void addAdministrator(User operator, String userName){

		if (!isAdministrator(operator)){
			throw new Forbidden();
		}
		try{
			User user = User.findArenaUserByName(userName);
			
		
			if (user !=null && !administrators.contains(user)){
				administrators.add(user);
			}
		}catch(javax.persistence.NoResultException e){
			
		}
	}
	
	public void removeAdministrator(User operator, User user){
		Builder protobuf = this.toProtobuf();
		if (!isAdministrator(operator)){
			throw new Forbidden();
		}
		administrators.remove(user);
		
	}


	public DomainModelView newRegistrationManagementView(User operator) {
		
		if (!isAdministrator(operator)){
			throw new Forbidden();
		}
		
		Builder builder = toProtobuf(1);
		
		builder.setAllowPlayerRegister(isAllowPlayerRegister());
		if (getExpectedRegistrationStartTime()!=null){
			builder.setExpectedRegistrationStartTime((int)(getExpectedRegistrationStartTime().getTime()/1000));
		}
		if (getExpectedRegistrationEndTime()!=null){
			builder.setExpectedRegistrationEndTime((int)getExpectedRegistrationEndTime().getTime()/1000);
		}
		builder.setAllowPlayerRegisterAfterPrepared(isAllowPlayerRegistrationAfterPrepared());
		builder.setEnableAutoExtraCheck(isAutoExtraCheck());
		if (isAutoExtraCheck()){
			Date startTime = getExpectedExtraCheckStartTime();
			Date endTime = getExpectedExtraCheckEndTime();
			if (startTime!=null && endTime!=null){
				builder.setAutoExtraCheckDuration((int)(
						new org.joda.time.Duration(
								new org.joda.time.DateTime(startTime),
								new org.joda.time.DateTime(endTime))
						.getMillis()/1000));
			}
		}
		
		//output required fields
		for(Entry<String, String> entry: PlayerInfoChecker.humanFriendlyFieldNames.entrySet()){
			String fieldName = entry.getKey();
			String fieldText = entry.getValue();
			proto.response.ResEvent.DynamicField.Builder builder2 = ResEvent.DynamicField.newBuilder()
			.setName(fieldName)
			.setText(fieldText);
			if (this.requiredPlayerInfo.contains(fieldName)){
				builder2.setSelected(true);
			}else{
				builder2.setSelected(false);	
			}
			builder.addRequiredPlayerInfo(builder2);
		}
		
		//output registered forces
		for (EventForce force: registeredForces){
			proto.response.ResEvent.EventForce.Builder builder2 = force.toProtobuf();
			
			
			for (String fieldName : requiredPlayerInfo){
				User user=null;
				if (force.getForceType().equals("user")){
					user=((RtsSingleUserEventForce)force).getUser();
				}else{
					user = ((RtsTeamEventForce)force).getTeam().getLeader();
				}
				
				proto.response.ResEvent.DynamicField.Builder builder3 = ResEvent.DynamicField.newBuilder()
				.setName(fieldName);
				String valueFromUser = PlayerInfoChecker.getValueFromUser(user, fieldName);
				if (valueFromUser == null){continue;}
				builder3.setValue(valueFromUser);
				builder2.addExtraFields(builder3);
			}
			builder.addRegisteredForces(builder2);
		}
		
		//output administrators
		for (User u : administrators){
			builder.addAdministrators(u.toProtobuf());
		}
		return new DomainModelView(builder);
		
		
	}
	
	public DomainModelView newRegistrationView(User operator) {
		Builder builder = toProtobuf(1);
		
		for(String fieldName:this.requiredPlayerInfo){
			String fieldText = PlayerInfoChecker.getHumanFriendlyFieldName(fieldName);
			proto.response.ResEvent.DynamicField.Builder builder2 = ResEvent.DynamicField.newBuilder()
			.setName(fieldName)
			.setText(fieldText);
			String value = PlayerInfoChecker.getValueFromUser(operator, fieldName);
			if (value!=null && !"".equals(value)){
				builder2.setValue(value);
			}
			builder.addRequiredPlayerInfo(builder2);
		}
		return new DomainModelView(builder);
	}
	
	public DomainModelView newDefaultView(User operator) {
		Builder builder = toProtobuf();
		if (isAdministrator(operator)){
			builder.addOperations("event.manage");
			builder.addOperations("event.registration.manage");
		}
		attachToPb(builder, operator);
		return new DomainModelView(builder);
	}

	public void deleteChildren() {
		for(RtsEliminationMatch subMatch:children){
			if (subMatch.getStatus().equals(Status.FINISHED)){
				throw new Forbidden("有些子赛事已经开始，禁止删除子赛事");
			}
		}
		for(RtsEventForce force:registeredForces){
			force.setBestRound(null);
			force.setNextMatchId(null);
		}
		for(RtsEliminationMatch subMatch:children){
			subMatch.remove();
		}
		this.children.clear();
		
	}

	@Override
	public DomainModelView newView(User operator, String viewName) {

		if (viewName.equals("default")){
			return newDefaultView(operator);
		}else if (viewName.equals("registration.manage")){
			return newRegistrationManagementView(operator);
		}else if (viewName.equals("register")){
			return newRegistrationView(operator);
		}else{
			throw new RuntimeException("can not reach here");
		}
	}

	@Override
	public DomainModelView newView(User operator) {
		return newView(operator,"default");
	}

	@Override
	public void assertPermission(User user, String name) throws Forbidden {
		
		if(!administrators.contains(user)){
			throw new Forbidden();
		}
		
	}
	
	@Override
	public boolean checkPermission(User user, String name) {
		
		return administrators.contains(user);
		
	}

	@Override
	public Iterable<User> findUsersByRole(String name) {
		return administrators;
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
		if (isAutoExtraCheck()){
			Date now = new Date();
			if (getExpectedExtraCheckStartTime()!=null){
				if (!now.after(getExpectedExtraCheckStartTime())){
					throw new Forbidden("已经过期.");
				}
			}
			if (getExpectedExtraCheckEndTime()!=null){
				if (!now.before(getExpectedExtraCheckEndTime())){
					throw new Forbidden("已经过期.");
				}
			}
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

	public void playerRegister(User player, RtsTeam team){
		
		if (!playerCanRegister()){
			throw new Forbidden("没有权限:已经过期");
		}
		
		if (this.defaultPlayGround!= null &&  !this.defaultPlayGround.getName().equals("--") && (
				this.defaultPlayGround.getUserName(player) == null ||
				this.defaultPlayGround.getUserName(player).isEmpty())){
			
			UserDetailedInfoRequired required = new UserDetailedInfoRequired("User Playground Info Required");
			required.setData(defaultPlayGround.getName());
			throw required;
		}
		
		PlayerInfoChecker playerInfoChecker = 
			new PlayerInfoChecker().setRequiredFields(this.requiredPlayerInfo);
		playerInfoChecker.check(player);
		
		
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
				throw new Forbidden("Not Leader");
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
	
}

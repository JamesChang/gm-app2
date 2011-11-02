package cn.gamemate.app.domain.event.rts;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResCampusArena;
import proto.response.ResEvent;
import proto.response.ResEvent.EventGet.Builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.DomainModelView;
import cn.gamemate.app.domain.Forbidden;
import cn.gamemate.app.domain.event.EntityEvent;
import cn.gamemate.app.domain.event.Event;
import cn.gamemate.app.domain.event.EventCenter;
import cn.gamemate.app.domain.event.EventForce;
import cn.gamemate.app.domain.event.PermissionSupport;
import cn.gamemate.app.domain.event.awards.UserEventData;
import cn.gamemate.app.domain.event.awards.UserEventModeData;
import cn.gamemate.app.domain.event.rts.RtsEliminationMatch.Status;
import cn.gamemate.app.domain.event.rts.RtsHome.LeaderBoard;
import cn.gamemate.app.domain.event.rts.RtsHome.LeaderBoardItem;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.DomainModelViewSupport;
import cn.gamemate.app.domain.event.rts.RtsLocation;
import cn.gamemate.app.domain.event.awards.UserEventData;


@RooJavaBean
@Entity
@RooEntity
@DiscriminatorValue("rr")
public class RoundRobin extends EntityEvent
	implements PlayerRegisterationSupport, DomainModelViewSupport, PermissionSupport,
	ChildrenManagementSupport{
	
	//- Fields ------------------------------------------------
	
	//protected RtsLocation defaultPlayGround;
	
	
	
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
	List<RtsEliminationMatch> children=Lists.newArrayList();
	
	@Basic
	java.util.Date creationDate = new Date();
	
	//- Helper Methods --------------------------------------------
	
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
		return "RoundRobin";
	}
	
	@Override
	public void userGenerateChildren(User operator){
		assertPermission(operator, null);
		generateChildren();
		
	}
	
	@Override
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
				
		Map<String, RtsEliminationMatch> rounds = Maps.newHashMap();
		
		int order = 1;
		
		int evenCount = (int)(Math.ceil(((float)allCount)/2)*2); 
        
		RtsEventForce[] s= new RtsEventForce[evenCount];
		registeredForces.toArray(s);
		
		for(int step=1;step<evenCount;step++){
			
		    int i =0,j=evenCount-1;
            while(i<j){
			
			//for (int i=0;i<evenCount;++i){
		//		int ti=(i+step)%evenCount;
		//		if (s[i]==null || s[ti]==null){
		//			continue;
	//			}
	/*			
				RtsEventForce homeForce = s[i];
				RtsEventForce awayForce = s[ti];
				s[0]=null;
				s[ti] = null;
	*/			
                RtsEventForce homeForce = s[i++];
                RtsEventForce awayForce = s[j--];
                if (homeForce == null || awayForce == null){
                  continue;
                }
				RtsEliminationMatch match = new RtsEliminationMatch();
				match.setName(this.getName() + "小组赛");
				match.setRequiredWin(defaultRequiredWin);
				if (defaultMaxRound!=null){
					match.setMaxRound(defaultMaxRound);
				}
				match.round = step;
				match.setGameId(this.gameId);
				/*if (this.map != null){
					match.setMap(this.map);
				}*/
				match.updateStatus();
				match.setChildOrder(order++);
				/*if (this.defaultPlayGround != null){
					match.setPlayGround(this.defaultPlayGround);
				}*/
				if (this.eventForceType!=null){match.setEventForceType(this.eventForceType);}
		        match.setPlayGround(new RtsLocation().setDiscriminatorValue("--"));		
				match.setHome(homeForce);
				match.setAway(awayForce);
					
				match.setParentId(getId());
				match.persist();
				children.add(match);
				//match.merge();
				
				
			}
            //loop
            RtsEventForce lastForce = s[evenCount-1];
            for (int k=evenCount-1;k>1;k--){
                s[k] = s[k-1];
            }
            s[1]=lastForce;
			
			
		}
        //init leaderboard
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
	
	//- Registration Support ----------------------------------------
	
	@Override
	public void playerRegister(User player, RtsTeam team) {
		if (this.eventForceType.equals(RtsEventForceType.USER)){
			if (player==null){
				throw new RuntimeException("user is null");
			}
			
			RtsSingleUserEventForce force = new RtsSingleUserEventForce(player);
				if (!registeredForces.contains(force)){
					force.persist();
					registeredForces.add(force);
			}
			
		}else if(this.eventForceType.equals(RtsEventForceType.TEAM)){
			if(team==null){
				throw new TeamRequiredException();
			}
			if (!player.equals(team.getLeader())){
				throw new Forbidden("没有权限：只有战队队长才能执行此操作");
			}
			
			RtsTeamEventForce force = new RtsTeamEventForce(team);
				if (!registeredForces.contains(force)){
					force.persist();
					registeredForces.add(force);
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

	
	//- Leader Board ---------------------------------------
	

	public LeaderBoard getLeaderboad(String mode){
		List<UserEventData> list = UserEventData.findForLeaderBoard(getId());
		LeaderBoard leaderBoard = new LeaderBoard(list);
		return leaderBoard;
	}
	
	
	public class LeaderBoard implements DomainModel{
		
		private List<LeaderBoardItem> list;
		
		public LeaderBoard(List<UserEventData> dataList) {
			list = Lists.newArrayList();
			for (UserEventData data:dataList){
				LeaderBoardItem item = new LeaderBoardItem();
				
				//TODO: trick
				
				if (eventForceType.equals(RtsEventForceType.USER)){
					
					User user = User.findUser(data.getUserID());
					item.setUsername(user.getName());
					item.setPortrait(user.getPortrait());
					item.setId(Integer.toString(user.getId()));
					item.setForceType("USER");
				}else{
					User user = User.findUser(data.getUserID());
					RtsTeam team = RtsTeam.findRtsTeamByUserGame(user, gameId);
					item.setUsername(team.getName());
					item.setPortrait(team.getImage());
					item.setId(team.getUuid());
					item.setForceType("TEAM");
				}
				item.setData(data);
				list.add(item);
			}
		}

		@Override
		public ResCampusArena.CA078_Leader_Board.Builder toProtobuf() {
			ResCampusArena.CA078_Leader_Board.Builder builder = 
				ResCampusArena.CA078_Leader_Board.newBuilder();
			for(LeaderBoardItem item: list){
				builder.addItems(item.toProtobuf());
			}
			return builder;
		}
		
	}
	
	public class LeaderBoardItem implements DomainModel{
		
		private String username;
		private String portrait;
		private String id;
		private UserEventData data;
		private String forceType;
		
		public String getForceType() {
			return forceType;
		}
		public void setForceType(String forceType) {
			this.forceType = forceType;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPortrait() {
			return portrait;
		}
		public void setPortrait(String portrait) {
			this.portrait = portrait;
		}
		public UserEventData getData() {
			return data;
		}
		public void setData(UserEventData data) {
			this.data = data;
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		@Override
		public ResCampusArena.CA078_Leader_Board_Item.Builder toProtobuf() {
			ResCampusArena.CA078_Leader_Board_Item.Builder builder = 
				ResCampusArena.CA078_Leader_Board_Item.newBuilder();
			builder.setName(username)
			.setImage(portrait)
			.setRtsScore(data.rtsScore)
			.setTotal(data.total)
			.setWin(data.win)
			.setDraw(data.draw)
			.setForceType(forceType)
			.setId(id)
			;
			return builder;
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
		
		if (verbose>=1){
			//output leaderboard
			cn.gamemate.app.domain.event.rts.RoundRobin.LeaderBoard leaderboad = getLeaderboad(null);
			builder.setCa078LeaderBoard(leaderboad.toProtobuf());

			//output registered forces
			for (EventForce force: registeredForces){
				builder.addRegisteredForces(force.toProtobuf());
			}
		}
		
		
		
		if (verbose>=2){
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
			
			for(Entry<Integer, proto.response.ResEvent.EliminationRound.Builder> entry
					:rounds.entrySet()){
				builder.addRounds(entry.getValue());
			}
			
			
		}
		return builder;
	}
	
	@Override
	public DomainModelView newView(User operator, String viewName) {
		if (viewName.equals("default")){
			return newDefaultView(operator);
		}else if (viewName.equals("leaderboard")){
			return newLeaderBoardView(operator);
		}else{
			throw new RuntimeException("can not reach here");
		}
	}
	

	@Override
	public DomainModelView newView(User operator) {
		return newView(operator,"default");
	}
	

	private DomainModelView newLeaderBoardView(User operator) {
		Builder builder = toProtobuf(1);
		
		return new DomainModelView(builder);
	}


	private DomainModelView newDefaultView(User operator) {
		
		Builder builder = toProtobuf();
		
		/*if (expectedTime !=null){
			builder.setExpectedTime(expectedTime);
		}*/
		
		return new DomainModelView(builder);
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
		
		/*RtsEventForce force = getPlayerEventForce(player);
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

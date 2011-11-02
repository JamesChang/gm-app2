package cn.gamemate.app.domain.event.rts;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResEvent;
import proto.response.ResEvent.EliminationBattle.Builder;
import proto.response.ResWar3Detail;
import proto.response.ResWar3Detail.War3Detail;
import cn.gamemate.app.domain.DomainModelView;
import cn.gamemate.app.domain.DomainModelViewSupport;
import cn.gamemate.app.domain.Forbidden;
import cn.gamemate.app.domain.Team;
import cn.gamemate.app.domain.event.EliminationMatch;
import cn.gamemate.app.domain.event.EntityEvent;
import cn.gamemate.app.domain.event.Event;
import cn.gamemate.app.domain.event.EventCenter;
import cn.gamemate.app.domain.event.EventForce;
import cn.gamemate.app.domain.event.PermissionSupport;
import cn.gamemate.app.domain.event.awards.AwardsPackage;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.common.PP;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;
import com.google.protobuf.InvalidProtocolBufferException;

@RooJavaBean
@Entity
@RooEntity
@DiscriminatorValue("rts_elm_match")
public class RtsEliminationMatch extends EliminationMatch implements
		PlayerSubmitResultSupport, DomainModelViewSupport{
	
	@Autowired
	@Transient
	EventCenter eventCenter;
	
	@Autowired(required=false)
	transient RtsAwardsPackageLoader awardsPackageLoader;
	
	/*
	public void setRtsAwardsPackage(AwardsPackage awardsPackages){
		super.setAwardsPackage(Lists.newArrayList(awardsPackages));
	}*/

	public enum Status {
		OPEN, FINISHED, NEW, READY
	};

	protected RtsLocation playGround;

	protected RtsEliminationMatch.Status status;
	
	
	@Override
	public String getEventType() {
		return "Match";
	}
	
	@Basic
	@NotNull
    private RtsEventForceType eventForceType;
	
	@Basic
	protected Integer round;
	
	@Basic
	protected int childOrder=0;

	@ManyToOne
	protected GameMap map;

	@Basic
	protected Integer gameId;

	@ElementCollection(fetch = FetchType.EAGER)
	Map<Integer, RtsBattle> battles = Maps.newHashMap();

	@Basic
	protected Integer nextWinnerMatchId;
	
	@Basic
	protected Integer nextLosserMatchId;

	@Basic
	protected Integer lastLeftMatchId;

	@Basic
	protected Integer lastRightMatchId;
	
	@Basic
	@Column(name="expected_time")
	protected String expectedTime;
	
	@Basic
	@Column(length=2048)
	protected String originalAwards;
	
	@Basic
	@Column(length=2048)
	protected String deltaAwards;

	public void setForce(RtsEliminationMatch match, EventForce force) {

		if (match.getId().equals(lastLeftMatchId)) {
			setHome(force);
		} else if (match.getId().equals(lastRightMatchId)) {
			setAway(force);
		} else {
			throw new RuntimeException();
		}

	}
	
	public boolean isAdministrator(User user){
		EntityEvent parent = getParent();
		if (parent ==null){return false;}
		PermissionSupport e = (PermissionSupport) parent;
		
		return e.checkPermission(user, null);
	}
	
	EntityEvent getParent(){
		if (this.parentId == null) return null;
		Event parent0 = eventCenter.getEvent(this.parentId);
		//RtsElimination parent = (RtsElimination) parent0;
		return (EntityEvent)parent0;
	}
		
	
	
	/*
	 * @Override public void refereeJudge(User referee, int winnerForce) {
	 * 
	 * //TODO: check referee's privilage //TODO: winnerForce must be 0 or 1
	 * 
	 * if (winnerForce == 0){ homeScore +=1.0f; }else if (winnerForce == 1){
	 * awayScore +=1.0f; }else{ throw new
	 * RuntimeException("can not reach here"); }
	 * 
	 * 
	 * }
	 */
	
	public boolean isUserRelevant(User user){
			if (isAdministrator(user)){
				return true;
			}
			if ((getHome()!=null && getHome().getPlayers().contains(user))|| 
				(getAway()!=null && getAway().getPlayers().contains(user))){
				return true;
			}else{
				return false;
			}
	}

	@Override
	public void playerSubmitResult(User player, RtsTeam team, Integer battleId,
			Integer winnerForce,byte[] pr) {

		if (battleId < 0 || battleId >= getMaxBatltleCount()) {
			throw new IllegalArgumentException("battleId");
		}

		Integer submitterForce = null;
		if (getHome()!=null && getAway()!=null){
			if (getHome().getForceType().equals("user")) {
				RtsSingleUserEventForce _homeForce = (RtsSingleUserEventForce) getHome();
				RtsSingleUserEventForce _awayForce = (RtsSingleUserEventForce) getAway();
				if (_homeForce.getUser().equals(player)) {
					submitterForce = 0;
				} else if (_awayForce.getUser().equals(player)) {
					submitterForce = 1;
				}
			} else if (getHome().getForceType().equals("team")) {
				if(team==null){
					if(!isAdministrator(player)){
						throw new TeamRequiredException();
					}
				}else{
					if (!player.equals(team.getLeader())){
						throw new Forbidden("没有权限：只有战队队长才能执行此操作");
					}
					RtsTeamEventForce _homeForce = (RtsTeamEventForce) getHome();
					RtsTeamEventForce _awayForce = (RtsTeamEventForce) getAway();
					if (_homeForce.getTeam().equals(team)) {
						submitterForce = 0;
					} else if (_awayForce.getTeam().equals(team)) {
						submitterForce = 1;
					}
				}
			}
		}

		if (submitterForce == null) {
			if (isAdministrator(player)){
				
			}else{
				
				throw new Forbidden();
			}
		}

		RtsBattle battle = battles.get(battleId);
		if (battle == null) {
			battle = newDummyBattle();
			battles.put(battleId, battle);
		}
		
		if (submitterForce == null){
			battle.setMandatoryWinner(winnerForce);
		}
		else if (submitterForce == 0) {

			battle.setHomeSubmitWinner(winnerForce);
		} else if (submitterForce == 1){
			battle.setAwaySubmitWinner(winnerForce);
		} 
		battle.setDisputed(battle.isDisputed());

		recalculateScore();
		
		if (pr !=null){
			War3Detail war3=null;
			try {
				war3 = ResWar3Detail.War3Detail.parseFrom(pr);
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (war3!=null){
				battle.setWar3DetailData(pr);
			}
			
		}


	}

	public Integer getWinnerForceId() {
		if (homeScore >= this.requiredWin) {
			return 0;
		} else if (awayScore >= this.requiredWin) {
			return 1;
		} else if (maxRound!=null && homeScore + awayScore >= maxRound){
			if (homeScore>awayScore){
				return 0;
			}else if (awayScore>homeScore){
				return 1;
			}else{
				return null;
			}
		}else {
			return null;
		}
	}

	public RtsEventForce getWinnerForce() {
		if (homeScore >= this.requiredWin) {
			return (RtsEventForce)home;
		} else if (awayScore >= this.requiredWin) {
			return (RtsEventForce)away;
		} else if (maxRound!=null && homeScore + awayScore >= maxRound){
			if (homeScore>awayScore){
				return (RtsEventForce)home;
			}else if (awayScore>homeScore){
				return (RtsEventForce)away;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public RtsEventForce getLosserForce() {
		
		if (homeScore >= this.requiredWin) {
			return (RtsEventForce)away;
		} else if (awayScore >= this.requiredWin) {
			return (RtsEventForce)home;
		} else if (maxRound!=null && homeScore + awayScore >= maxRound){
			if (homeScore>awayScore){
				return (RtsEventForce)away;
			}else if (awayScore>homeScore){
				return (RtsEventForce)home;
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}

	protected void recalculateScore() {
		Status oldStatus = getStatus();

		float _homeScore = 0.0f, _awayScore = 0.0f;
		for (Entry<Integer, RtsBattle> battleEntry : battles.entrySet()) {
			RtsBattle _battle = battleEntry.getValue();
			Integer _winnerForce = _battle.getWinnerForce();
			if (_winnerForce != null){
				if (_winnerForce.equals(0)) {
					_homeScore += 1;
				} else {
					_awayScore += 1;
				}
				
			}
		}
		if (this.homeScore != _homeScore) {
			this.homeScore = _homeScore;
		}
		if (this.awayScore != _awayScore) {
			this.awayScore = _awayScore;
		}

		updateStatus();

		// update next round event
		//if (getStatus() != oldStatus) {
		if (true){

			if (this.nextWinnerMatchId != null) {
				EliminationMatch nextMatch0 = RtsEliminationMatch
						.findEliminationMatch(this.nextWinnerMatchId);
				RtsEliminationMatch nextMatch = (RtsEliminationMatch) nextMatch0;
				RtsEventForce winnerForce = getWinnerForce();
				
				if (winnerForce!=null){
					
					winnerForce.setNextMatchId(nextMatch.getId());
					winnerForce.setBestRound(nextMatch.getRound());
					
				}/*else{
					((RtsEventForce)home).setNextMatchId(this.getId());
					((RtsEventForce)home).setBestRound(this.getRound());
					((RtsEventForce)away).setNextMatchId(this.getId());
					((RtsEventForce)away).setBestRound(this.getRound());
				}*/
				      
				nextMatch.setForce(this, winnerForce);
				nextMatch.battles.clear();
				nextMatch.setHomeScore(0.0f);
				nextMatch.setAwayScore(0.0f);
				nextMatch.updateStatus();
			}
				
			if (this.nextLosserMatchId != null){
				EliminationMatch nextMatch0 = RtsEliminationMatch
				.findEliminationMatch(this.nextLosserMatchId);
				RtsEliminationMatch nextMatch = (RtsEliminationMatch) nextMatch0;
				
				RtsEventForce losserForce = (RtsEventForce)getLosserForce();
				
				if (losserForce!=null){
					losserForce.setNextMatchId(nextMatch.getId());
					losserForce.setBestRound(nextMatch.getRound());
					
				}/*else{
					((RtsEventForce)home).setNextMatchId(this.getId());
					((RtsEventForce)home).setBestRound(this.getRound());
					((RtsEventForce)away).setNextMatchId(this.getId());
					((RtsEventForce)away).setBestRound(this.getRound());
				}*/
				      
				nextMatch.setForce(this, getLosserForce());
				nextMatch.battles.clear();
				nextMatch.setHomeScore(0.0f);
				nextMatch.setAwayScore(0.0f);
				nextMatch.updateStatus();
			}
			
			RtsEventForce winnerForce = getWinnerForce();
			RtsEventForce losserForce = (RtsEventForce)getLosserForce();
			if (status.equals(Status.FINISHED)){
				//update awards
				if (awardsPackageLoader!=null){
					for (AwardsPackage p0: awardsPackageLoader.getAwardsPackage(this)){
						RtsAwardsPackage p = (RtsAwardsPackage)p0;
						p.calculateAndUpdate(new RtsAwardsCalculatorContext(this));
					}
				}
			}
			
			if (winnerForce!=null){
				
				

				EntityEvent parent = getParent();
				if (parent instanceof RtsElimination){
					
					if (this.round.equals(2)){
						winnerForce.setNextMatchId(null);
						winnerForce.setBestRound(1);
						losserForce.setNextMatchId(null);
						losserForce.setBestRound(2);
						
					}else if (this.round.equals(34)){
						winnerForce.setNextMatchId(null);
						winnerForce.setBestRound(3);
						losserForce.setNextMatchId(null);
						losserForce.setBestRound(4);
					}else{
						losserForce.setNextMatchId(null);
						losserForce.setBestRound(this.getRound());
					}
				}
				
			}else{
				((RtsEventForce)home).setNextMatchId(this.getId());
				((RtsEventForce)home).setBestRound(this.getRound());
				((RtsEventForce)away).setNextMatchId(this.getId());
				((RtsEventForce)away).setBestRound(this.getRound());
			}
			
		}

	}

	protected void updateStatus() {
		
		if (home != null && away!=null){
			if (homeScore >= this.requiredWin || awayScore >= this.requiredWin ||
					(this.maxRound !=null && (homeScore + awayScore) >=this.maxRound)) {
				this.setStatus(Status.FINISHED);
			} else {
				this.setStatus(Status.READY);
			}
		}else{
			this.setStatus(Status.OPEN);
		}
		
		
	}


	public Game getGame() {
		if (gameId != null) {
			return Game.findById(gameId);
		} else {
			return null;
		}
	}

	public int getMaxBatltleCount() {
		if (maxRound!=null){
			return Math.min(this.requiredWin * 2 - 1, maxRound);
		}
		else{
			return this.requiredWin * 2 - 1;
		}
	}

	@Override
	public ResEvent.EventGet.Builder toProtobuf(int verbose) {
		ResEvent.EventGet.Builder builder = super.toProtobuf(verbose);
		
		if (status != null) {
			builder.setStatus(status.toString());
		}
		if (expectedTime !=null){
			builder.setExpectedTime(expectedTime);
		}
		
		if (eventForceType !=null){
			builder.setEventForceType(eventForceType.toString());
		}
		
		if (verbose <2) return builder;
		if (map != null) {
			builder.setMap(map.toProtobuf());
		}
		if (playGround != null) {
			builder.setPlayGround(playGround.getDiscriminatorValue());
		}
		if (getGame() != null) {
			builder.setLogicalGame(getGame().toProtobuf());
		}
		
		

		for(int i=0;i<getMaxBatltleCount();i++){
			RtsBattle battle=battles.get(i);
			if (battle==null){
				if (this.status.equals(Status.FINISHED)){
					break;
				}else{
					battle=newDummyBattle();
				}
			}
			Builder battleBuilder=battle.toProtobuf().setSequence(i);
			builder.addBattle(battleBuilder);
		}
		
		/*
		for (Entry<Integer, RtsBattle> battleEntry : battles.entrySet()) {
			Builder battleBuilder = battleEntry.getValue().toProtobuf();
			Integer key = battleEntry.getKey();
			battleBuilder.setSequence(key);
			builder.addBattle(battleBuilder);
		}
*/
		return builder;
	}
	
	private RtsBattle newDummyBattle(){
		RtsBattle battle = new RtsBattle();
		if (this.map!=null) {battle.setMapName(map.getName());}
		if (this.expectedTime!=null) {battle.setExpectedTime(expectedTime.toString());}
		return battle;
	}
		
	public void adminUpdateBattle(User admin, Integer battleId, 
			String mapName, String expectedTime) {
		if (!isAdministrator(admin)){
			throw new Forbidden();
		}

		if (battleId < 0 || battleId >= getMaxBatltleCount()) {
			throw new IllegalArgumentException("battleId");
		}

		RtsBattle battle = battles.get(battleId);
		if (battle == null) {
			battle = newDummyBattle();
			battles.put(battleId, battle);
		}
		
		if (mapName !=null){
			battle.setMapName(mapName);
		}
		if (expectedTime!=null){
			battle.setExpectedTime(expectedTime);
		}
	}

	public static List<RtsEliminationMatch> findUpcomingMatches(User user, Integer gameId){
		return new RtsJdbcHelper().getPlayerUpcomingMatches(user, gameId);
	}
	

	public static List<RtsEliminationMatch> findFinishedMatches(User user, Integer gameId){
		return new RtsJdbcHelper().getPlayerFinishedMatches(user, gameId);
	}

	public EventForce getHome() {
		return home;
	}
	public EventForce getAway() {
		return away;
	}

	@Override
	public DomainModelView newView(User operator, String viewName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomainModelView newView(User operator) {
		proto.response.ResEvent.EventGet.Builder builder = toProtobuf();
		
		EntityEvent parent0 = getParent();
		
		if (isUserRelevant(operator) && parent0!=null ){
			
			if (parent0 instanceof  PlayerRegisterationSupport){
				PlayerRegisterationSupport parent = (PlayerRegisterationSupport) parent0;
				
				//Add players extra info
				if (home!=null){
					proto.response.ResEvent.EventForce.Builder homeBuilder = home.toProtobuf();
					home.attatchExtraInfo(homeBuilder, parent.getRequiredPlayerInfo());
					builder.setHome(homeBuilder);
				}
				if (away!=null){
					proto.response.ResEvent.EventForce.Builder awayBuilder = away.toProtobuf();
					away.attatchExtraInfo(awayBuilder, parent.getRequiredPlayerInfo());
					builder.setAway(awayBuilder);
				}
			}
			
			//add referees extra info
			if (parent0 instanceof  PlayerRegisterationSupport){
				PermissionSupport parent = (PermissionSupport) parent0;
				for (User u : parent.findUsersByRole(null)){
					proto.response.ResUser.UserModel.Builder builder2 = u.toProtobuf();
					if (u.getQq()!=null){
						builder2.setImQq(u.getQq());
					}
					if (u.getMobile()!=null){
						builder2.setMobile(u.getMobile());
					}
					builder.addAdministrators(builder2);
				}
			}
			
			//add operations
			if (isAdministrator(operator)){
				builder.addOperations("event.manage");
			}
			builder.addOperations("event.submit_result");
		}
		return new DomainModelView(builder);
	}

	public void forceQuit(RtsEventForce force) {
		if (this.status.equals(Status.FINISHED)){return;}
		
		
	}


	
}

package cn.gamemate.app.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proto.response.ResEvent;
import proto.response.ResEvent.UserEventList;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelView;
import cn.gamemate.app.domain.DomainModelViewSupport;
import cn.gamemate.app.domain.Forbidden;
import cn.gamemate.app.domain.NotFullySupportedException;
import cn.gamemate.app.domain.ObjectNotFound;
import cn.gamemate.app.domain.UserOperationNotSupported;
import cn.gamemate.app.domain.event.EntityEvent;
import cn.gamemate.app.domain.event.Event;
import cn.gamemate.app.domain.event.EventCenter;
import cn.gamemate.app.domain.event.Hall;
import cn.gamemate.app.domain.event.Ladder;
import cn.gamemate.app.domain.event.PermissionSupport;
import cn.gamemate.app.domain.event.rts.GroupElimination;
import cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport;
import cn.gamemate.app.domain.event.rts.RtsBattle;
import cn.gamemate.app.domain.event.rts.RtsElimination;
import cn.gamemate.app.domain.event.rts.RtsEliminationMatch;
import cn.gamemate.app.domain.event.rts.RtsEventForceType;
import cn.gamemate.app.domain.event.rts.RtsHome.LeaderBoard;
import cn.gamemate.app.domain.event.rts.RtsLocation;
import cn.gamemate.app.domain.event.rts.RtsTeam;
import cn.gamemate.app.domain.party.DefaultParty;
import cn.gamemate.app.domain.party.PartyManager;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.protobuf.GeneratedMessage.Builder;


@RequestMapping("/events")
@Controller
public class EventController {
	
	@Autowired
	private EventCenter eventCenter;
	@Autowired(required=true)
	private UserRepository userRepository;
	
	@Autowired(required=true)
	private PartyManager partyManager;
	

	
	@RequestMapping(value="/{id}/list")
	public String list( @PathVariable("id") Integer eventId, @RequestParam(required=false) String stick, ModelMap modelMap){
		Event event = eventCenter.getEvent(eventId);
		
		if (!(event instanceof Hall)){
			throw new UserOperationNotSupported();
		}
		Hall hall = (Hall) event;
        modelMap.addAttribute("object", hall.getArenaList(stick));
        modelMap.addAttribute("subpb", "ca03ArenaList");
        return "";
	}
	
	class EventList implements DomainModel{
		
		private List<Event> eventList;
		private User viewer;
		
		public User getViewer() {
			return viewer;
		}

		public void setViewer(User viewer) {
			this.viewer = viewer;
		}

		public EventList() {
			this.eventList = Lists.newArrayList();
		}

		public EventList copyFromIdList(Iterable<String> eventIdList) {
			for (String id: eventIdList){
				Integer eventId=null;
				try{
					eventId=Integer.parseInt(id);
				}catch(NumberFormatException e){
					continue;
				}
				Event event = getEvent(eventId);
				eventList.add(event);
			}
			return this;
		};
		
		public EventList copyFromEventList(List<? extends Event> eventList){
			for (Event event:eventList){
				this.eventList.add(event);
			}
			return this;
		}
		

		@Override
		public com.google.protobuf.GeneratedMessage.Builder toProtobuf() {
			UserEventList.Builder builder = UserEventList.newBuilder();
			for (Event event: eventList){
				if (event != null){
					proto.response.ResEvent.EventGet.Builder builder2 = event.toProtobuf(1);
					if (viewer !=null && event instanceof RtsElimination){
						RtsElimination rtsElimination = (RtsElimination) event;
						rtsElimination.attachToPb(builder2, viewer);
					}
					builder.addEvents(builder2);
				}
					
			}
			return builder;
		}
		
		
	}
	
	private Event getEvent(Integer id){
		return eventCenter.getEvent(id);
	}
	
	@RequestMapping(value="/multiget")
	public String multiget(@RequestParam(value="events") String eventIdString, ModelMap modelMap){
		Iterable<String> event_ids = Splitter.on(',').split(eventIdString);
		modelMap.addAttribute("object", new EventList().copyFromIdList(event_ids));
		modelMap.addAttribute("subpb", "my_events");
		return "";
	}
	

	@RequestMapping(value="/0/rts_user_registered")
	@Transactional
	public String playerRegisteredEvents(
			@RequestParam(value="userid") Integer userId,
			@RequestParam(value="team", required=false) String teamId,
			ModelMap modelMap
			){
		User operator = User.findUser(userId);
		RtsTeam team = RtsTeam.findRtsTeam(teamId);
		List<RtsElimination> events = cn.gamemate.app.domain.event.rts.RtsElimination.getPlayerRegisteredEvents(operator,team);
		EventList eventList = new EventList().copyFromEventList(events);
		eventList.setViewer(operator);
		modelMap.addAttribute("object", eventList);
		modelMap.addAttribute("subpb", "my_events");
		return "";
		
	}
	
	@RequestMapping(value="/0/rts_user_team_registered")
	@Transactional
	public String teamRegisteredEvents(
			@RequestParam(value="userid") Integer userId,
			@RequestParam(value="team") String teamId,
			ModelMap modelMap
			){
		User operator = User.findUser(userId);
		RtsTeam team = RtsTeam.findRtsTeam(teamId);
		List<RtsElimination> events = cn.gamemate.app.domain.event.rts.RtsElimination.getTeamRegisteredEvents(team);
		modelMap.addAttribute("object", new EventList().copyFromEventList(events));
		modelMap.addAttribute("subpb", "my_events");
		return "";
		
	}
	
	@RequestMapping(value="/0/upcoming_matches")
	@Transactional
	public String upcomingMatches(
			@RequestParam(value="userid") Integer userId,
			@RequestParam(value="target") Integer targetId,
			@RequestParam(value="game") Integer gameId,
			ModelMap modelMap
			){
		User operator = User.findUser(userId);
		User target = User.findUser(targetId);
		List<RtsEliminationMatch> events = cn.gamemate.app.domain.event.rts.RtsEliminationMatch.findUpcomingMatches(target, gameId);
		modelMap.addAttribute("object", new EventList().copyFromEventList(events));
		modelMap.addAttribute("subpb", "my_events");
		return "";
		
	}
	

	@RequestMapping(value="/0/finished_matches")
	@Transactional
	public String finishedMatches(
			@RequestParam(value="userid") Integer userId,
			@RequestParam(value="target") Integer targetId,
			@RequestParam(value="game") Integer gameId,
			ModelMap modelMap
			){
		User operator = User.findUser(userId);
		User target = User.findUser(targetId);
		List<RtsEliminationMatch> events = cn.gamemate.app.domain.event.rts.RtsEliminationMatch.findFinishedMatches(target, gameId);
		modelMap.addAttribute("object", new EventList().copyFromEventList(events));
		modelMap.addAttribute("subpb", "my_events");
		return "";
		
	}


	class EventWrapper implements DomainModel{
		
		private ResEvent.EventGet.Builder eventBuilder;

		
		
		public EventWrapper(
				proto.response.ResEvent.EventGet.Builder eventBuilder) {
			super();
			this.eventBuilder = eventBuilder;
		}



		@Override
		public Builder toProtobuf() {
			return eventBuilder;
		}
		
	}
	
	@RequestMapping(value="/{id}")
	public String get( @PathVariable("id") Integer eventId, @RequestParam(value="userid", required=false) Integer userId, ModelMap modelMap){
		Event event = eventCenter.getEvent(eventId);
		
		if (userId!=null && event instanceof DomainModelViewSupport){
			DomainModelViewSupport viewSupport = (DomainModelViewSupport)event;
			User operator = User.findUser(userId);
			DomainModelView view = viewSupport.newView(operator);
			modelMap.addAttribute("object", view);
		}
		else{
			modelMap.addAttribute("object", event);
		}
		
        modelMap.addAttribute("subpb", "eventGet");
        return "";
	}
	
	@RequestMapping(value="/{id}/create")
	public String create(@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId, 
			@RequestParam(required=false) String mode, 
			@RequestParam(value="map", required=false) Integer mapId, 
			@RequestParam(value="private", required=false) boolean isPrivate,
			@RequestParam(value="attributes", required=false) String leaderAttributes,
			@RequestParam(value="name", required=false) String name,
			ModelMap modelMap){
		
		Event event0 = eventCenter.getEvent(eventId);
		
		if (event0 instanceof Hall){
			
			Hall event = (Hall) event0;
			User operator = userRepository.getUser(userId);
			DefaultParty party = partyManager.getParty(operator, false);
			if (party == null){
				event.userCreateArena(operator, mode, mapId, name);
			}
			else{
				event.partyCreateArena(operator, mode, mapId, name, false);
			}
			
			
		}else{
			Ladder event = (Ladder) event0;
			User operator = userRepository.getUser(userId);
			TreeMap<String, String> leaderAttrMap = new TreeMap<String, String>();
			if (leaderAttributes !=null && !leaderAttributes.equals("")){
				Iterator<String> iterator = Splitter.on('=').split(leaderAttributes).iterator();
				while(true){
					if (!iterator.hasNext()) break;
					String k = iterator.next();
					System.out.println(k);
					if (!iterator.hasNext()) break;
					String v = iterator.next();
					System.out.println(v);
					leaderAttrMap.put(k, v);
				}
			}
			DefaultParty party = partyManager.getParty(operator, false);
			if (party == null){
				event.singleJoin(operator, mode, leaderAttrMap);
			}
			else{
				event.partyJoin(operator, mode, leaderAttrMap);
			}
			
		}
		
		return "";
		
	}
	
	@RequestMapping(value="/{id}/leave")
	public String leave(@RequestParam(value="userid") Integer userId, @PathVariable("id") Integer eventId){
		
		
		
		Event event0 = eventCenter.getEvent(eventId);
		if (event0 instanceof Ladder){
			Ladder ladder = (Ladder) event0;
			User operator = userRepository.getUser(userId);
			ladder.userLeave(operator);
			return "";
		}else{
			throw new UserOperationNotSupported();
		}
	}
	
	@RequestMapping(value="/{id}/rts_submit_result")
	@Transactional
	public String rtsPlayerSubmitResult(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="team", required=false) String teamId,
			@RequestParam(value="sequence") Integer sequence,
			@RequestParam(value="winner") Integer winnerForce,
			@RequestParam(value="pr", required=false) String pr
			){
		
		User operator = User.findUser(userId);
		RtsTeam team = null;
		if (teamId != null){
			team = RtsTeam.findRtsTeam(teamId);
		}
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.PlayerSubmitResultSupport)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.PlayerSubmitResultSupport event = 
			(cn.gamemate.app.domain.event.rts.PlayerSubmitResultSupport) event0;
		
		if (pr!=null){
			byte[] ret= new byte[pr.length()/2];
			byte[] bs = pr.getBytes();
			for(int i=0;i<bs.length;i+=2){
				byte b = bs[i];
				int a1 = b-48 <=9 ? b-48 : b-55;
				b = bs[i+1];
				int a2 = b-48 <=9 ? b-48 : b-55;
				int t = a1*16 + a2;
				ret[i/2]=(byte)t;
			}
			event.playerSubmitResult(operator, team, sequence, winnerForce, ret);
		}
		
		event.playerSubmitResult(operator, team, sequence, winnerForce,null);
		return "";
	}
	
	@RequestMapping(value="/{id}/register")
	@Transactional
	public String playerRegister(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="team", required=false) String teamId
			){
		User operator = User.findUser(userId);
		RtsTeam team = null;
		if (teamId != null){
			team = RtsTeam.findRtsTeam(teamId);
		}
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport event = 
			(cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport) event0;
		event.playerRegister(operator, team);
		return "";
	}
	
	@RequestMapping(value="/{id}/checkin")
	@Transactional
	public String playerCheckin(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId
			){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport event = 
			(cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport) event0;
		event.playerCheckIn(operator, null);
		return "";
	}
	
	@RequestMapping(value="/{id}/register/page")
	@Transactional
	public String playerRegisterPage(
			@PathVariable("id") Integer eventId,
			@RequestParam(value="userid") Integer userId,
			ModelMap modelMap
			){
		User operator = User.findUser(userId);
		RtsElimination match;
		match = RtsElimination.findRtsElimination(eventId);
		if (match == null){
			throw new ObjectNotFound(RtsElimination.class, eventId);
		}
		DomainModelView modelView = match.newView(operator, "register");
		modelMap.addAttribute("object", modelView);
		modelMap.addAttribute("subpb", "eventGet");
		return null;
	}
	
	@RequestMapping(value="/{id}/unregister")
	@Transactional
	public String playerUnregister(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="team", required=false) String teamId
			){
		User operator = User.findUser(userId);
		RtsTeam team = null;
		if (teamId != null){
			team = RtsTeam.findRtsTeam(teamId);
		}
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport event = 
			(cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport) event0;
		event.playerUnregister(operator, team);
		return "";
	}
	
	@RequestMapping(value="/{id}/unregister_a")
	@Transactional
	public String administratorUnregister(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="team", required=false) String targetTeamId,
			@RequestParam(value="user", required=false) Integer targetUserId
			){
		User operator = User.findUser(userId);
		RtsTeam team = null;
		User player = null;
		if (targetTeamId != null){
			team = RtsTeam.findRtsTeam(targetTeamId);
		}
		if (targetUserId != null){
			player = User.findUser(targetUserId);
		}
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport event = 
			(cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport) event0;
		event.administratorUnregister(operator, player, team);
		return "";
	}
	
	
	@RequestMapping(value="/{id}/generate_children")
	@Transactional
	public String adminGenerateChildren(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.ChildrenManagementSupport)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.ChildrenManagementSupport event = 
			(cn.gamemate.app.domain.event.rts.ChildrenManagementSupport) event0;
		
		event.userGenerateChildren(operator);
		return "";
		
	}
	
	@RequestMapping(value="/{id}/delete_children")
	@Transactional
	public String adminDeleteGenerateChildren(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.RtsElimination)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.RtsElimination event = 
			(cn.gamemate.app.domain.event.rts.RtsElimination) event0;
		if (!event.isAdministrator(operator)){
			throw new Forbidden();
		}
		event.deleteChildren();
		return "";
		
	}
	
	@RequestMapping(value="/{id}/rts_update")
	@Transactional
	public String admin_rts_update(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="expected_time", required=false) String expectedTime,
			@RequestParam(value="required_win", required=false) Integer requiredWin
			){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.RtsEliminationMatch)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.RtsEliminationMatch event = 
			(cn.gamemate.app.domain.event.rts.RtsEliminationMatch) event0;
		if (!event.isAdministrator(operator)){
			throw new Forbidden();
		}
		for(RtsBattle battle:event.getBattles().values()){
			if (battle.getWinnerForce()!=null){
				throw new NotFullySupportedException("this match has began");
			}
		}
		/*if (event.getBattles().size()>0){
			throw new NotFullySupportedException("this match has began");
		}*/
		if (name !=null){
			event.setName(name);
		}
		if (expectedTime !=null){
			event.setExpectedTime(expectedTime);
		}
		if (requiredWin !=null){
			event.setRequiredWin(requiredWin);
		}
		return "";
		
	}
	
	@RequestMapping(value="/{id}/rts_update_battle")
	@Transactional
	public String updateBattle(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="sequence") Integer sequence,
			@RequestParam(value="expected_time", required=false) String expectedTime,
			@RequestParam(value="map_name", required=false) String mapName
			){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.RtsEliminationMatch)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.RtsEliminationMatch event = 
			(cn.gamemate.app.domain.event.rts.RtsEliminationMatch) event0;
		event.adminUpdateBattle(operator, sequence, mapName, expectedTime);
		return "";
		
	}
	
	@RequestMapping(value="/create_rts_event")
	@Transactional
	public String createRtsEvent(
		@RequestParam(value="event", required=false) Integer event_id,
		@RequestParam(value="name", required=false) String name,
		@RequestParam(value="game_id", required=false) Integer gameId,
		@RequestParam(value="force_type", required=false) String forceType,
		@RequestParam(value="userid") Integer userId,
		@RequestParam(value="required_win", defaultValue="1") Integer requiredWin,
		@RequestParam(value="expected_time", required=false) String expectedTime,
		@RequestParam(value="playground", defaultValue="--") String playground,
		@RequestParam(value="rules", required=false) String rules,
        ModelMap modelMap
		){
		User operator = User.findUser(userId);
		
		
		RtsElimination match;
		if (event_id == null){
			match = new RtsElimination();
			match.getAdministrators().add(operator);
			match.setCreationDate(new java.util.Date());
			
			//trick
			List<String> superUsers = Arrays.asList("y1","user1", "flash90", "huangzhe");
			if (!superUsers.contains(operator.getName())){
				throw new Forbidden();
			}
			
			
			
		}else{
			match = RtsElimination.findRtsElimination(event_id);
			if (!match.isAdministrator(operator)){
				throw new Forbidden();
			}
		}
		if (name !=null) match.setName(name);
		match.setDefaultRequiredWin(requiredWin);
		if (forceType!=null) match.setEventForceType(RtsEventForceType.valueOf(forceType.toUpperCase()));
		if (gameId!=null){match.setGameId(gameId);}
		
		match.setDefaultRequiredWin(requiredWin);
		if (expectedTime!=null){
			match.setExpectedTime(expectedTime);
		}
		if (rules!=null){
			match.setRules(rules);
		}
        match.setDefaultPlayGround(new RtsLocation().setDiscriminatorValue(playground));
		match.persist();

        modelMap.addAttribute("object",match);
        modelMap.addAttribute("subpb","eventGet");
		return null;
	}
	
	@RequestMapping(value="/{id}/manage_registration_strategy")
	@Transactional
	public String updateEliminationRegistrationSettings(
			@PathVariable("id") Integer eventId,
			@RequestParam(value="userid") Integer userId,
			
			@RequestParam(value="allow_player_register", required=false) Boolean allowPlayerRegister,
			@RequestParam(value="expected_registration_start_time", required=false) String expectedRegistrationStartTime,
			@RequestParam(value="expected_registration_end_time", required=false) String expectedRegistrationEndTime,
			@RequestParam(value="allow_player_register_after_prepared", required=false) Boolean allowPlayerRegistrationAfterPrepared,
			
			@RequestParam(value="registration_limit", required=false) Integer registrationLimit,
			@RequestParam(value="registration_deadline", required=false) String registrationDeadline,
			@RequestParam(value="required_info", required=false) String requiredInfo,
			@RequestParam(value="enable_auto_extra_check", required=false) Boolean enable_auto_extra_check,
			@RequestParam(value="auto_extra_check_duration", required=false) Integer auto_extra_check_duration
			){
		User operator = User.findUser(userId);
		EntityEvent event0;
		event0 = EntityEvent.findEntityEvent(eventId);
		if (event0 == null){
			throw new ObjectNotFound(EntityEvent.class, eventId);
		}
		if (!(event0 instanceof PlayerRegisterationSupport)){
			throw new UserOperationNotSupported("这个赛事不支持此操作");
		}
		
		PlayerRegisterationSupport event = (PlayerRegisterationSupport)event0;
		
		//TODO: refactoring required.
		if (event instanceof PermissionSupport){
			PermissionSupport e = (PermissionSupport) event;
			e.assertPermission(operator, null);
		}
			
		if (allowPlayerRegister!=null){
			event.setAllowPlayerRegister(allowPlayerRegister);
		}
		
		if (expectedRegistrationStartTime !=null && !expectedRegistrationStartTime.equals("")){
			event.setExpectedRegistrationStartTime(new org.joda.time.DateTime(
					expectedRegistrationStartTime).toDate());
		}
		
		//deprecated
		if (registrationDeadline != null && !registrationDeadline.equals("")) {
			event.setExpectedRegistrationEndTime(new org.joda.time.DateTime(
					registrationDeadline).toDate());
		}
		
		if (expectedRegistrationEndTime !=null && !expectedRegistrationEndTime.equals("")){
			event.setExpectedRegistrationEndTime(new org.joda.time.DateTime(
					expectedRegistrationEndTime).toDate());
		}
		

		if (allowPlayerRegistrationAfterPrepared!=null){
			event.setAllowPlayerRegistrationAfterPrepared(allowPlayerRegistrationAfterPrepared);
		}
		
		
		if (registrationLimit !=null){
			event.setRegistrationLimit(registrationLimit);
		}
		
		if (requiredInfo !=null){
          if (!requiredInfo.equals("")){
			List<String> requiredInfoList = Lists.newArrayList(Splitter.on(',').split(requiredInfo));
			event.setRequiredPlayerInfo(requiredInfoList);
          }else{
        	  event.setRequiredPlayerInfo(new ArrayList<String>());
          }
		}
		
		if (enable_auto_extra_check !=null){
			event.setAutoExtraCheck(enable_auto_extra_check);
			if (auto_extra_check_duration !=null){
				Date registrationEndTime = event.getExpectedRegistrationEndTime();
				event.setExpectedExtraCheckEndTime(registrationEndTime);
				Duration duration = new org.joda.time.Duration(auto_extra_check_duration * 1000);
				event.setExpectedExtraCheckStartTime(
						new org.joda.time.DateTime(registrationDeadline).minus(duration).toDate());
			}
		}
		
		event0.merge();
		return null;
	}
	
	@RequestMapping(value="/{id}/manage_registration_strategy/page")
	@Transactional
	public String updateEliminationRegistrationSettingsPage(
			@PathVariable("id") Integer eventId,
			@RequestParam(value="userid") Integer userId,
			ModelMap modelMap
			){
		User operator = User.findUser(userId);
		EntityEvent event;
		event = EntityEvent.findEntityEvent(eventId);
		if (event == null){
			throw new ObjectNotFound(RtsElimination.class, eventId);
		}
		if (!(event instanceof DomainModelViewSupport)){
			throw new UserOperationNotSupported();
		}
		DomainModelView modelView = ((DomainModelViewSupport)event).newView(operator, "registration.manage");
		modelMap.addAttribute("object", modelView);
		modelMap.addAttribute("subpb", "eventGet");
		return null;
	}
	
	@RequestMapping(value="/{id}/rts_batch_mutate_children")
	@Transactional
	public String batchMutateChildren(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="round") Integer round,
			@RequestParam(value="expected_time", required=false) String expectedTime,
			@RequestParam(value="required_win", required=false) Integer requiredWin){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.RtsElimination)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.RtsElimination event = 
			(cn.gamemate.app.domain.event.rts.RtsElimination) event0;
		event.batchMutateChildren(operator, round, requiredWin, expectedTime);
		return "";
	}
	
	@RequestMapping(value="/{id}/add_administrator")
	@Transactional
	public String add_admin(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="target_user_name") String targetUserName
			){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.RtsElimination)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.RtsElimination event = 
			(cn.gamemate.app.domain.event.rts.RtsElimination) event0;
		event.addAdministrator(operator, targetUserName);
		return "";
	}
	
	@RequestMapping(value="/{id}/remove_administrator")
	@Transactional
	public String remove_admin(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="target_user") Integer targetUserId
			){
		User operator = User.findUser(userId);
		User user = User.findUser(targetUserId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.RtsElimination)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.RtsElimination event = 
			(cn.gamemate.app.domain.event.rts.RtsElimination) event0;
		event.removeAdministrator(operator, user);
		return "";
	}
	
	@RequestMapping(value="/{id}/rts_leaderboard")
	@Transactional
	public String remove_admin(
			//@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="mode") String mode,
			ModelMap modelMap
			){
		//User operator = User.findUser(userId);
		
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.RtsHome)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.RtsHome event = 
			(cn.gamemate.app.domain.event.rts.RtsHome) event0;
		LeaderBoard leaderBoard = event.getLeaderboad(mode);

		modelMap.addAttribute("object", leaderBoard);
		modelMap.addAttribute("subpb", "rts_leaderboard");
		return "";
	}
	
	
	//- Grouping Management ---------------------------------------------
	
	@RequestMapping(value="/{id}/new_group")
	@Transactional
	public String newGroup(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="name") String name
			){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.GroupElimination)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.GroupElimination event = 
			(cn.gamemate.app.domain.event.rts.GroupElimination) event0;
		event.newChildGroup(operator, name);
		return "";
	}
	
	@RequestMapping(value="/{id}/delete_group")
	@Transactional
	public String deleteGroup(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="name") String name
			){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.GroupElimination)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.GroupElimination event = 
			(cn.gamemate.app.domain.event.rts.GroupElimination) event0;
		event.deleteChildGroup(operator, name);
		return "";
	}
	
	@RequestMapping(value="/{id}/move_force")
	@Transactional
	public String moveForceToGroup(
			@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId,
			@RequestParam(value="force") Long forceId,
			@RequestParam(value="name") String name
			){
		User operator = User.findUser(userId);
		Event event0 = eventCenter.getEvent(eventId);
		if (!(event0 instanceof cn.gamemate.app.domain.event.rts.GroupElimination)){
			throw new UserOperationNotSupported();
		}
		cn.gamemate.app.domain.event.rts.GroupElimination event = 
			(cn.gamemate.app.domain.event.rts.GroupElimination) event0;
		event.moveForceToGroup(operator, forceId, name);
		return "";
	}
	
	
	//- Group Elimination ---------------------------------------------
	
	@RequestMapping(value="/create_group_elimination")
	@Transactional
	public String createRtsEvent(
		@RequestParam(value="name") String name,
		@RequestParam(value="game_id") Integer gameId,
		@RequestParam(value="force_type", required=false) String forceType,
		@RequestParam(value="userid") Integer userId,
		@RequestParam(value="required_win", defaultValue="1") Integer requiredWin,
		@RequestParam(value="max_round", defaultValue="1") Integer maxRound,
		@RequestParam(value="expected_time", required=false) String expectedTime,
		@RequestParam(value="playground", defaultValue="--") String playground,
		@RequestParam(value="rules", required=false) String rules,
        ModelMap modelMap
		){
		User operator = User.findUser(userId);
		
		
		GroupElimination match;
		match = new GroupElimination();
		match.getAdministrators().add(operator);
		//match.setCreationDate(new java.util.Date());
			
			//trick
			List<String> superUsers = Arrays.asList("y1","user1", "flash90", "huangzhe");
			if (!superUsers.contains(operator.getName())){
				throw new Forbidden();
			}
			
			
		if (name !=null) match.setName(name);
		match.setDefaultRequiredWin(requiredWin);
		if (forceType!=null) match.setEventForceType(RtsEventForceType.valueOf(forceType.toUpperCase()));
		if (gameId!=null){match.setGameId(gameId);}
		//match.setRegisterationLimit(registrationLimit);
		match.setDefaultRequiredWin(requiredWin);
		
		if (rules!=null){
			match.setRules(rules);
		}
        /*if (registrationDeadline != null && !registrationDeadline.equals("")){
  		    match.setRegisterationDeadline(new org.joda.time.DateTime(registrationDeadline).toDate());
        }*/
		//match.setDefaultPlayGround(new RtsLocation().setDiscriminatorValue(playground));
		match.persist();

        modelMap.addAttribute("object", match);
        modelMap.addAttribute("subpb", "eventGet");
		return null;
			
	}
	
}


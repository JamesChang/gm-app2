package cn.gamemate.app.domain.event;

import static cn.gamemate.app.domain.arena.Arena.ArenaStatus.GAMING;
import static cn.gamemate.app.domain.arena.Arena.ArenaStatus.OPEN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import proto.res.ResArena.Arena.Builder;
import proto.response.ResGameres.GameMessage;
import proto.response.ResSc.StarCraftData;
import proto.response.ResWar3Detail.War3Detail;
import proto.response.ResWar3Detail.War3Detail.DotaPlayer;
import proto.response.ResWar3Detail.War3Detail.RPGDota;
import proto.util.Util.StringDictItem;
import cn.gamemate.app.clientmsg.MessageService;
import cn.gamemate.app.clientmsg.RelayServices;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.Forbidden;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.ArenaForce;
import cn.gamemate.app.domain.arena.ArenaFullException;
import cn.gamemate.app.domain.arena.ArenaSlot;
import cn.gamemate.app.domain.arena.ArenaStatusChangedEvent;
import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.arena.UserLeavedArenaMessage;
import cn.gamemate.app.domain.arena.UserNotInArenaException;
import cn.gamemate.app.domain.arena.msg.ArenaChatMessage;
import cn.gamemate.app.domain.arena.msg.ArenaEndedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaInvitationMessage;
import cn.gamemate.app.domain.arena.msg.ArenaJoinedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaLeaderChangedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaLeavedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaMemberUpdatedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaSlotLockUpdatedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaStartMessage;
import cn.gamemate.app.domain.arena.msg.ArenaStatusUpdated;
import cn.gamemate.app.domain.arena.msg.ArenaUserAttributeUpdatedMessage;
import cn.gamemate.app.domain.arena.msg.UserJoinedArenaMessage;
import cn.gamemate.app.domain.user.AlertMessage;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.User.UserStatus;
import cn.gamemate.common.PP;

import com.google.protobuf.InvalidProtocolBufferException;

public class Arena05 extends Arena {

	protected User leader;
	protected String userStatusEx="";
	
	private java.util.Random random = new Random(System.currentTimeMillis());
	


	static 
	public void assertUserNotInArena(User operator){
		if (operator.getArenaId() != null){
			throw new DomainModelRuntimeException("Target user has already joined arena");
		}
	}

	public void assertStatus(ArenaStatus status){
		if (this.status != status)
			throw new Forbidden("arena is "+this.status.toString() + " now, but "+ status.toString() + "is required.");
	}
	
	/**
	 * change arena status, send messages to client.
	 * 
	 * @param status
	 */
	public void updateStatus(ArenaStatus status){
		
		if (this.status != status){
			ArenaStatus oldStatus = this.status;
			this.status = status;
			new ArenaStatusUpdated(this).send();
			
			for (ArenaSlot slot: allSlots){
				if (slot.getUser()!=null){
					new ArenaMemberUpdatedMessage(this, slot.getUser(), true, false, true, true).send();
				}
			}
			fireEvent(new ArenaStatusChangedEvent(this, oldStatus));
			
		}
			
	}
	
	public void updateAllPlayerStatus(UserStatus userStatus){
		for(ArenaSlot slot:allSlots){
			if (slot.getUser()!=null){
				slot.getUser().setStatus(userStatus);
			}
		}
	}
	
	public void updatePlayerStatusEx(User user){
		user.setStatusEx(this.userStatusEx);
	}
	
	protected void unreadyAll(){
		for (ArenaSlot slot: allSlots){
			slot.setReady(false);
		}
	}
	protected void unGamingAll(){
		for (ArenaSlot slot: allSlots){
			slot.setGaming(false);
		}
	}

	private void autoElectLeader(User leavedUser) {
		if (leavedUser.equals(leader)) {
			for (ArenaSlot slot : allSlots) {
				if (slot.getUser() != null) {
					assert slot.getPosition() != 0;
					User newLeader = slot.getUser();
					setLeader(newLeader);
					moveUser(newLeader, slot.getPosition(), 0);
					new ArenaLeaderChangedMessage(this, leader).send();
					new ArenaMemberUpdatedMessage(this, newLeader, false, true,
							true, true).send();
					break;
				}
			}
		}
	}

	private void autoClose() {
		if (getPlayerCount() == 0) {
			close();
		}
	}

	public void setLeader(User user) {
		leader = user;
	}

	public User getLeader() {
		return leader;
	}

	/**
	 * find a empty and enabled slot, and let the user sit into it.
	 * 
	 * 
	 * @param user
	 * @return returns the slot in witch the user is, or null if this method
	 *         failed.
	 */
	protected ArenaSlot addPlayer(User user) {
		ArenaSlot targetSlot = null;
		// if user is already in
		for (ArenaSlot slot : allSlots) {
			if (slot.getUser() != null && slot.getUser().equals(user)
					&& slot.isEnabled()) {
				targetSlot = slot;
				break;
			}
		}
		// get a empty available slot
		if (targetSlot == null) {
			for (ArenaSlot slot : allSlots) {
				if (slot.getUser() == null && slot.isEnabled()) {
					targetSlot = slot;
					break;
				}
			}
		}
		// sit in
		if (targetSlot != null) {
			targetSlot.setUser(user);
			updatePlayerStatusEx(user);
		}
		return targetSlot;

	}

	protected User removeUser(User target) {
		ArenaSlot slot = getUserSlot(target);
		if (slot != null) {
			slot.clear();
			target.setArenaId(null);
			updatePlayerStatusEx(target);
		}
		return target;
	}

	private void moveUser(User operator, int position, int targetSlotId) {
		if (allSlots.size() > position 
				&& allSlots.size() > targetSlotId
				&& operator != null && allSlots.get(targetSlotId).isEnabled()
				&& operator.equals(allSlots.get(position).getUser())
				&& allSlots.get(targetSlotId).getUser() == null) {
			ArenaSlot srcSlot = allSlots.get(position);
			ArenaSlot tarSlot = allSlots.get(targetSlotId);
			tarSlot.clear();
			tarSlot.setUser(operator);
			tarSlot.getExtra().putAll(srcSlot.getExtra());
			srcSlot.clear();
		} else {
			throw new DomainModelRuntimeException("can not change slot");
		}

	}

	private static final List<String> normalActons = new ArrayList<String>();
	private static final List<String> leaderActons = new ArrayList<String>();
	private static final List<String> leaderStartActons = new ArrayList<String>();
	private static final List<String> chatActions = new ArrayList<String>();
	static {
		chatActions.add("chat");
		
		normalActons.add("chslot");
		normalActons.add("leave");
		normalActons.add("chat");
		normalActons.add("setUserAttr");
		normalActons.add("ready");
		normalActons.add("invite");
		
		leaderActons.add("leave");
		leaderActons.add("chat");
		leaderActons.add("setUserAttr");
		leaderActons.add("invite");
		leaderActons.add("kick");
		leaderActons.add("lockSlot");
		leaderActons.add("chslot");
		leaderActons.add("unlockSlot");
		
		leaderStartActons.addAll(leaderActons);
		leaderStartActons.add("start");

	}
	
	/**
	 * checking if all the players are ready
	 * @return
	 */
	private boolean  checkPlayerReady(){
		for (ArenaSlot slot : allSlots) {
			if (slot.getUser() != null
					&& !slot.getUser().equals(this.leader)
					&& !slot.isReady()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * checking if all the players are online
	 * @return
	 */
	private boolean checkPlayerStatus(){
		for (ArenaSlot slot : allSlots) {
			if (slot.getUser() != null){
				UserStatus userStatus = slot.getUser().getStatus();
				// to make UAS work, we have to allow a browsing user 
				if (userStatus != User.UserStatus.ONLINE && userStatus != User.UserStatus.BROWSING){
					logger.debug(slot.getUser().getName() + " " + userStatus);
					return false;
				}
			}
		}
		return true;
	}
	
	protected void assertUserPermission(User user, String action){
		
		List<String> permissions = getUserAvailableActions(user);
		if (!permissions.contains(action)){
			throw new Forbidden("permission denied.");
		}
			
	}

	public List<String> getUserAvailableActions(User user) {
		// only chat if arena is not open
		if (this.status != OPEN){
			ArenaSlot userSlot = getUserSlot(user);
			if (userSlot == null){
				throw new DomainModelRuntimeException("User not in this arena");
			}
			if (userSlot.isGaming()){
				return chatActions;
			}else{
				return normalActons;
			}
		}
		if (user.equals(leader)) {
			boolean canStart = true;

			canStart = checkPlayerReady();
			if (canStart){
				canStart = checkPlayerStatus();
				if (!canStart) {
					logger.debug("user status check failed.");
				}
			}

			if (!isNumOfForceGT2()) {
				canStart = false;
				logger.debug("num of force <2");
			}

			if (canStart) {
				//logger.debug("can start");
				return leaderStartActons;
			} else {
				return leaderActons;
			}

		}
		return normalActons;

	}

	protected boolean isNumOfForceGT2() {
		// checking if num of force >=2
		ArenaForce f = null;
		int forceCount = 0;
		for (ArenaSlot slot : slots) {
			if (slot.getUser() != null && !slot.getForce().equals(f)) {
				forceCount += 1;
				f = slot.getForce();
			}
		}
		return forceCount >= 2;
	}


	@Override
	public Builder toProtobuf() {

		Builder protobuf = super.toProtobuf();
		if (leader != null) {
			protobuf.addAttributes(StringDictItem.newBuilder().setKey("leader")
					.setValue(String.valueOf(this.leader.getId())));
			
		}
		return protobuf;
	}

	protected void assertLeader(User user) {
		if (leader != null && leader.equals(user))
			return;
		throw new Forbidden("must be leader");
	}

	// ///////////////////////////////////////
	// Service Layer
	// //////////////////////////////////////

	synchronized public void userEnter(User operator) {

		// for a user already in
		if (operator.getArenaId() != null
				&& operator.getArenaId().equals(this.getInt32Id())) {

		} else {
			assertUserNotInArena(operator);
			//if (event != null)
				//event.assertUserAvailable(operator);
		}
		
		assertStatus(OPEN);
		
		MessageService leaderRelayService = RelayServices.getRelayService(leader);
		MessageService myRelayService = RelayServices.getRelayService(operator);
		if (myRelayService != leaderRelayService){
			new AlertMessage(operator, "无法加入游戏。因为游戏主机在另一个服务器", true);
			throw new DomainModelRuntimeException("game host is at a different server");
		}

		ArenaSlot slot = addPlayer(operator);
		if (slot == null) {
			throw new ArenaFullException();
		}
		//TODO: check and set
		operator.setArenaId(this.getInt32Id());

		new ArenaJoinedMessage(this, operator).send();
		new UserJoinedArenaMessage(this, operator).send();
		new ArenaMemberUpdatedMessage(this, operator, false, false, true, false)
				.send();
		// TODO: FriendDataChanged Message
		if (leader != null)
			new ArenaMemberUpdatedMessage(this, leader, false, false, true,
					false).send();

	}
	
	synchronized protected void _userLeave(User operator){
		removeUser(operator);

		new ArenaLeavedMessage(this, operator).send();
		new UserLeavedArenaMessage(this, operator).send();
		// TODO: FriendDataChangedMessage
		autoElectLeader(operator);
		autoClose();
		if (leader != null && status != ArenaStatus.CLOSED)
			new ArenaMemberUpdatedMessage(this, leader, false, false, true,
					false).send();
		checkUserLiveness(operator);
	}

	synchronized public void userLeave(User operator) {
		assertUserPermission(operator, "leave");
		_userLeave(operator);
		
	}

	synchronized public void userChangeSlot(User operator, int targetSlotId) {
		assertUserPermission(operator, "chslot");
		ArenaSlot slot = getUserSlot(operator);
		if (slot == null) {
			throw new UserNotInArenaException();
		}
		moveUser(operator, slot.getPosition(), targetSlotId);
		new ArenaMemberUpdatedMessage(this, operator, false, true, false, true)
				.send();
		if (leader != null)
			new ArenaMemberUpdatedMessage(this, leader, false, false, true,
					false).send();
	}

	synchronized public void userReady(User operator) {
		assertUserPermission(operator, "ready");
		ArenaSlot slot = getUserSlot(operator);
		if (slot == null) {
			throw new UserNotInArenaException();
		}
		slot.toggleReady();
		new ArenaMemberUpdatedMessage(this, operator, false, false, true, true)
				.send();
		if (leader != null)
			new ArenaMemberUpdatedMessage(this, leader, false, false, true,
					false).send();

	}

	synchronized public void userSetAttribute(User operator, String key,
			String value) {
		assertUserPermission(operator, "setUserAttr");
		ArenaSlot slot = getUserSlot(operator);
		if (slot == null) {
			throw new UserNotInArenaException();
		}
		// TODO: validation
		slot.getExtra().put(key, value);
		new ArenaUserAttributeUpdatedMessage(this, operator, key, value).send();
	}
	
	public synchronized void userSetArenaAttribute(User operator, String key, String value) {
		assertStatus(OPEN);
		assertLeader(operator);
		if (key.equals("private")) {
			if (value.equals("true")) {
				synchronized (this) {
					assertLeader(operator);
					assertStatus(OPEN);
					setPrivate(true);
					new ArenaStatusUpdated(this).send();
				}
				return;
			} else if (value.equals("false")) {
				synchronized (this) {
					assertLeader(operator);
					assertStatus(OPEN);
					setPrivate(false);
					new ArenaStatusUpdated(this).send();
				}
				return;
			}
		}
		throw new DomainModelRuntimeException("attribute not support.");
	}
	
	synchronized public void userStart(User operator) {
		assertStatus(OPEN);
		assertLeader(operator);
		if (!checkPlayerReady()){
			throw new DomainModelRuntimeException(
			"not all the players are ready");	
		}
		if (!checkPlayerStatus()){
			throw new DomainModelRuntimeException(
			"not all the players are online");
		}
		if (!isNumOfForceGT2()) {
			throw new DomainModelRuntimeException(
					"can not start arena because all users are in the same force");
		}
		
		attributes.put("hostID", String.valueOf(this.leader.getId()));
		// TODO: FriendStatusChanged
		for (ArenaSlot slot:slots){
			if (slot.getUser() != null){
				slot.setGaming(true);
			}
		}
		updateStatus(GAMING);
		proto.res.ResArena.Arena arenaSnapshot = this.toProtobuf().setName(Integer.toString(random.nextInt(100000000))).build();
		Battle battle = Battle.createAndSave(arenaSnapshot);
		this.lastBattle = battle;
		ArenaStartMessage arenaStartMessage = new ArenaStartMessage(this, arenaSnapshot);
		arenaStartMessage.send();
	}
	
	public synchronized void userCancel(User operator) {
		// DO nothing
	}
	
	synchronized public void userSubmitResultL(Integer winnerForce){
		if (this.status!= GAMING){
			return;
		}
		new ArenaEndedMessage(this, winnerForce).send();
		end();
	}

	synchronized public void userSubmitResult(byte[] result) {

		GameMessage r = null;
		try {
			r = GameMessage.parseFrom(result);
			logger.debug("game result in {}: {}", this.getInt32Id(), r);
			
		} catch (InvalidProtocolBufferException e) {
			logger.error("Can not parse Protobuf", e);
			PP.println(result);
			throw new DomainModelRuntimeException("error on parsing result");
			
		} catch (IOException e) {
			logger.error("Can not parse Protobuf", e);
		}

		// TODO: logging
		if (this.status!= GAMING){
			return;
		}
		
		for (War3Detail w : r.getWar3List()) {
			submit_war3_result(w, r);
		}
		for (StarCraftData w : r.getStarcraftList()) {
			submit_sc_result(w, r);
		}
	}

	private void submit_war3_result(War3Detail w, GameMessage r) {
		
		if (w.getHeader().getMessageType().equals("game result")) {
			
			//Add user field to "Player" in result
			GameMessage.Builder builder = GameMessage.newBuilder();
			proto.response.ResWar3Detail.War3Detail.Builder war3Builder = War3Detail.newBuilder(w);
			for (int i =0;i<w.getDotasCount();++i){
				RPGDota dota = w.getDotas(i);
				proto.response.ResWar3Detail.War3Detail.RPGDota.Builder nDota = RPGDota.newBuilder(dota);
				User u = User.findUser(nDota.getPlayer().getUserID());
				nDota.setPlayer(DotaPlayer.newBuilder(dota.getPlayer()).setUser(u.toProtobuf()));
				war3Builder.setDotas(i, nDota);
			}
			for (int i = 0;i<w.getBattlesCount();i++){
				//the same with the above loop
				User u = User.findUser(w.getBattles(i).getPalyer().getUserID());
				war3Builder.setBattles(i, 
					war3Builder.getBattles(i).toBuilder().setPalyer(				
						w.getBattles(i).getPalyer().toBuilder().setUser(u.toProtobuf())
					)
				);
			}
			builder.addWar3(war3Builder);
			new ArenaEndedMessage(this, builder.build()).send();
			end();
		}

	}

	private void submit_sc_result(StarCraftData w, GameMessage r) {
		if (w.getHeader().getMessageType().equals("game result")) {
			GameMessage.Builder builder = GameMessage.newBuilder();
			proto.response.ResSc.StarCraftData.Builder newBuilder = StarCraftData.newBuilder(w);
			for (int i =0;i<w.getPlayersCount();++i){
				User u = User.findUser(w.getPlayers(i).getUserID());
				newBuilder.setPlayers(i, 
						w.getPlayers(i).toBuilder().setUser(u.toProtobuf())
				);
			}
			builder.addStarcraft(newBuilder);
			new ArenaEndedMessage(this, builder.build()).send();
			end();
			
		}

	}
	
	protected void end(){
		unreadyAll();
		unGamingAll();
		updateStatus(OPEN);
	}

	@Deprecated
	synchronized public void userError(User operator) {
		removeUser(operator);
		new ArenaLeavedMessage(this, operator).send();
		new UserLeavedArenaMessage(this, operator).send();
		// TODO: FriendDataChangedMessage
		autoElectLeader(operator);
		autoClose();
		if (leader != null)
			new ArenaMemberUpdatedMessage(this, leader, false, false, true,
					false).send();

	}
	
	private void checkUserLiveness(User user){
		if (this.status != GAMING){
			return;
		}
		
		//check if all players in one force has quit.
		boolean [] forceLiviness = new boolean[forces.size()];
		Integer winningForce= null;
		for (ArenaSlot s: slots){
			if (s.getUser() != null && s.isGaming()) {
				forceLiviness[s.getForce().getId()]=true;
			}
		}
		//TODO: to support more force
		for(int i=0;i<2;i++){
			if(forceLiviness[i]==false && forceLiviness[1-i]==true){
				winningForce = 1-i;
				
			}
		}
		if (winningForce == null){
			new ArenaEndedMessage(user.getId(), this, "游戏正在进行中").send();
		}else{
			new ArenaEndedMessage(this, winningForce).send();
			new ArenaChatMessage(this, user.getName() + " 离开了游戏").send();
			end();
		}
	}
	
	synchronized public void userQuitGame(User operator) {
		if (this.status != GAMING){
			return;
		}
		ArenaSlot slot = getUserSlot(operator);
		if (slot == null) {
			throw new UserNotInArenaException();
		}
		slot.setGaming(false);
		slot.setReady(false);
		new ArenaMemberUpdatedMessage(this, operator, true, false, true,
				true).send();
		
		//check if all players in one force has quit.
		boolean [] forceLiviness = new boolean[forces.size()];
		Integer winningForce= null;
		for (ArenaSlot s: slots){
			if (s.getUser() != null && s.isGaming()) {
				forceLiviness[s.getForce().getId()]=true;
			}
		}
		checkUserLiveness(operator);
		
	}

	synchronized public void userChat(User user, String content) {
		ArenaSlot slot = getUserSlot(user);
		if (slot == null) {
			throw new UserNotInArenaException();
		}
		new ArenaChatMessage(this, user, content).send();
	}

	synchronized public void userKick(User operator, User target) {
		assertStatus(OPEN);
		assertLeader(operator);
		User removed = removeUser(target);
		if (removed ==null) return;
		new ArenaLeavedMessage(this, target).send();
		new UserLeavedArenaMessage(this, target).send();
		// TODO: FriendDataChanged
		if (leader != null)
			new ArenaMemberUpdatedMessage(this, leader, false, false, true,
					false).send();
	}
	
	synchronized public void userLockSlot(User operator, Integer slotid){
		assertStatus(OPEN);
		assertLeader(operator);
		ArenaSlot mySlot = getUserSlot(operator);
		if (mySlot.getPosition() == slotid){
			throw new DomainModelRuntimeException("can not lock yourself's slot");
		}
		if (slotid < 0 || slotid>= allSlots.size()){
			throw new DomainModelRuntimeException("position of slot out of range");
		}
		ArenaSlot slot = allSlots.get(slotid);
		if (slot.getUser() != null){
			throw new DomainModelRuntimeException("can not lock non-empty slot");
		}
		slot.disable();
		new ArenaSlotLockUpdatedMessage(this, slot).send();
	}

	public void userUnlockSlot(User operator, Integer slotid) {
		assertStatus(OPEN);
		assertLeader(operator);
		if (slotid < 0 || slotid>= allSlots.size()){
			throw new DomainModelRuntimeException("position of slot out of range");
		}
		ArenaSlot slot = allSlots.get(slotid);
		slot.enable();
		new ArenaSlotLockUpdatedMessage(this, slot).send();
	}
	
	public void userInvite(User operator, User target){
		assertUserPermission(operator, "chslot");
		assertUserNotInArena(target);
		if (event != null)
			event.assertAvailable(target);
		new ArenaInvitationMessage(event, this, operator, target).send();
		
	}

	synchronized public void netError(String errorMsg){
		new ArenaEndedMessage(this, errorMsg).send();
		end();
	}


}

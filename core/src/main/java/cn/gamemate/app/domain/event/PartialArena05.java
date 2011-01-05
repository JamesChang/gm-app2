package cn.gamemate.app.domain.event;

import static cn.gamemate.app.domain.arena.Arena.ArenaStatus.MATCHING;
import static cn.gamemate.app.domain.arena.Arena.ArenaStatus.OPEN;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.NotSupportedException;

import cn.gamemate.app.domain.NotFullySupportedException;
import cn.gamemate.app.domain.arena.ArenaSlot;
import cn.gamemate.app.domain.user.User;

/**
 * @author jameszhang
 * Arena with only one 
 */
public class PartialArena05 extends Arena05 implements Party{
	
	public PartialArena05() {
		super();
		type = "partial";
	}
	
	
	private static final List<String> normalActons = new ArrayList<String>();
	private static final List<String> leaderActons = new ArrayList<String>();
	private static final List<String> leaderStartActons = new ArrayList<String>();
	private static final List<String> chatActions = new ArrayList<String>();
	static {
		chatActions.add("chat");
		chatActions.add("cancel");
		normalActons.add("chslot");
		normalActons.add("leave");
		normalActons.add("chat");
		normalActons.add("setUserAttr");
		normalActons.add("ready");
		leaderActons.add("leave");
		leaderActons.add("chat");
		leaderActons.add("setUserAttr");
		leaderActons.add("kick");
		//leaderActons.add("lockSlot");
		//leaderActons.add("unlockSlot");
		leaderStartActons.addAll(leaderActons);
		leaderStartActons.add("start");

	}

	public List<String> getUserAvailableActions(User user) {
		// only chat if arena is not open
		if (this.status != OPEN){
			return chatActions;
		}
		if (user.equals(leader)) {
			boolean canStart = true;

			// checking all ready
			for (ArenaSlot slot : slots) {
				if (slot.getUser() != null
						&& !slot.getUser().equals(this.leader)
						&& !slot.isReady()) {
					canStart = false;
					logger.info("{} is not ready", slot.getUser());
					break;
				}
			}

			// checking user status
			for (ArenaSlot slot : slots) {
				if (slot.getUser() != null
						&& slot.getUser().getStatus() != User.UserStatus.ONLINE) {
					canStart = false;
					logger.info("{} is not online", slot.getUser());
					break;
				}
			}


			if (canStart) {
				logger.info("can start");
				return leaderStartActons;
			} else {
				return leaderActons;
			}

		}
		return normalActons;

	}
	
	// ///////////////////////////////////////
	// Service Layer
	// //////////////////////////////////////
	

	@Override
	public synchronized void userStart(User operator) {
		assertStatus(OPEN);
		assertLeader(operator);

		
		//attributes.put("hostID", String.valueOf(this.leader.getId()));
		// TODO: FriendStatusChanged
		updateStatus(MATCHING);
		
	}
	
	@Override
	public synchronized void userSubmitResult(byte[] result) {
		super.userSubmitResult(result);
		//throw new NotFullySupportedException("need more judgement");
	}
	
	protected void end(){
		super.end();
	}
	
	@Override
	public synchronized void userCancel(User operator) {
		assertStatus(MATCHING);
		
		updateStatus(OPEN);
	}
	
	@Override
	public synchronized void userLeave(User operator) {
		updateStatus(OPEN);
		super._userLeave(operator);
	}
}

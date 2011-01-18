package cn.gamemate.app.domain.arena.msg;

import java.util.ArrayList;

import proto.msg.MsgArena.ArenaInvitation;
import proto.msg.MsgArena.ArenaInvitation.Builder;
import proto.msg.MsgParty.EventInvitation;
import proto.response.ResUser.UserModel;
import cn.gamemate.app.clientmsg.AnswerableClientMessage;
import cn.gamemate.app.domain.arena.UserSlot;
import cn.gamemate.app.domain.event.Arena05;
import cn.gamemate.app.domain.event.SimpleHall;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.party.DefaultParty;
import cn.gamemate.app.domain.party.LadderInvitationDeclinedMessage;
import cn.gamemate.app.domain.user.User;

public class ArenaInvitationMessageEx extends AnswerableClientMessage {

	private DefaultParty party;
	private boolean isPrivate;
	private String customName;
	private Integer mapId;
	private String mode;
	private SimpleHall event;

	public ArenaInvitationMessageEx(final DefaultParty iParty,
			SimpleHall event, String mode, Integer mapId,
			String customName, boolean isPrivate) {
		party = iParty;
		this.event = event;
		this.mode = mode;
		this.mapId = mapId;
		this.customName = customName;
		this.isPrivate = isPrivate;
		party.setReceivers(receivers);
		receivers.remove(party.getLeaderId());
		Builder builder = ArenaInvitation.newBuilder()
		.setInviter(UserModel.newBuilder()
				.setId(party.getLeaderId())
				.setName(party.getLeaderSlot().getUser().getName())
				.setPortrait(party.getLeaderSlot().getUser().getPortrait())
				)
		.setArenaName(event.getName());
		builder.setEvent(
				event.toProtobuf()
				.addRequiredMaps(GameMap.findGameMap(mapId.longValue()).toProtobuf()));
		rootBuilder.setArenaInvitation(builder);
	}

	@Override
	protected void answerCallback(User user, String answer) {
		if (answer.equals("no")) {
			ArrayList<Integer> receivers = new ArrayList<Integer>();
			for (UserSlot userSlot : party.getMembers()) {
				if (userSlot.getUser().getId() != user.getId())
					receivers.add(userSlot.getUser().getId());
			}
			event.releaseParty(party);
			tag = "no";
			new LadderInvitationDeclinedMessage(receivers, party,
					user.getName() + " 拒绝了这次游戏").send();
		} else {
			if (answeredUsers.size() == receivers.size()) {
				tag = "yes";
				event.releaseParty(party);
				Arena05 arena = event.userCreateArena(party.getLeaderSlot().getUser(),
						mode, mapId, customName, isPrivate);
				for (UserSlot userSlot : party.getMembers()) {
					if (userSlot.getUser().getId() != party.getLeaderId())
						arena.userEnter(userSlot.getUser());
				}
				
			}

		}

	}

	@Override
	protected void onTimeOut() {
		if (tag != null)
			return;
		event.releaseParty(party);
		ArrayList<Integer> receivers = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();
		receivers.add(party.getLeaderId());
		for (UserSlot userSlot: party.getMembers()){
			if (answeredUsers.contains(userSlot.getUser().getId())){
				receivers.add(userSlot.getUser().getId());
			}else if (userSlot.getUser().getId() != party.getLeaderId()){
				sb.append(" ").append(userSlot.getUser().getName());
			}
		}
		sb.append("没有回应");
		new LadderInvitationDeclinedMessage(receivers, party, sb.toString()).send();
	}

	@Override
	public int getCode() {
		return 0x2306;
	}

}

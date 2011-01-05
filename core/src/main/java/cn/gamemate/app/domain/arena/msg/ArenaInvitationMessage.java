package cn.gamemate.app.domain.arena.msg;

import proto.msg.MsgArena.ArenaInvitation;
import proto.msg.MsgArena.ArenaInvitation.Builder;
import proto.response.ResUser.UserModel;
import cn.gamemate.app.clientmsg.AnswerableClientMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.event.Arena05;
import cn.gamemate.app.domain.event.Event;
import cn.gamemate.app.domain.user.User;

public class ArenaInvitationMessage extends AnswerableClientMessage{

	public ArenaInvitationMessage(Event event, 
			Arena arena, User inviter, User target) {
		receivers.add(target.getId());
		Builder builder = ArenaInvitation.newBuilder()
		.setArenaID(arena.getInt32Id())
		.setTargetUserID(target.getId())
		.setInviter(UserModel.newBuilder()
				.setId(inviter.getId())
				.setName(inviter.getName())
				.setPortrait(inviter.getPortrait())
				)
		.setArenaName(arena.getName());
		builder.setEvent(
				event.toProtobuf()
				.addRequiredMaps(arena.getGameMap().toProtobuf()));
		rootBuilder.setArenaInvitation(builder
				);
	}
	
	@Override
	public int getCode() {
		return 0x2306;
	}
	
	@Override
	public int getAge() {
		return 16;
	}

	@Override
	protected void answerCallback(User user, String answer) {
		if (answer.equals("yes")) {
			int arenaID = getMsg().getArenaInvitation().getArenaID();
			Arena05 arena =(Arena05)Arena05.findArena(arenaID);
			arena.userEnter(user);
		}
	}

	@Override
	protected void onTimeOut() {
		
	}

}

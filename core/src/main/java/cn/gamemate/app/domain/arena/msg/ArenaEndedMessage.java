package cn.gamemate.app.domain.arena.msg;

import proto.msg.MsgArena.ArenaEnded;
import proto.response.ResGameres.GameMessage;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.arena.Arena;

public class ArenaEndedMessage extends ClientMessage{
	
	public ArenaEndedMessage(Arena arena, GameMessage result){
		arena.setUserIdList(receivers);
		rootBuilder.setArenaEnded(ArenaEnded.newBuilder().setArenaID(arena.getInt32Id()).setResult(result));
	}
	
	public ArenaEndedMessage(Arena arena, Integer winnerForce){
		arena.setUserIdList(receivers);
		rootBuilder.setArenaEnded(ArenaEnded.newBuilder().setArenaID(arena.getInt32Id()).setWinnerForce(winnerForce));
	}
	
	public ArenaEndedMessage(Arena arena, String errorMsg) {
		arena.setUserIdList(receivers);
		rootBuilder.setArenaEnded(
				ArenaEnded.newBuilder()
				.setArenaID(arena.getInt32Id()).setError(errorMsg));
	}

	public ArenaEndedMessage(Integer receiver, Arena arena, String errorMsg) {
		receivers.add(receiver);
		rootBuilder.setArenaEnded(
				ArenaEnded.newBuilder()
				.setArenaID(arena.getInt32Id()).setError(errorMsg));
	}
	

	@Override
	public int getCode() {

		return 0x230D;
	}

}

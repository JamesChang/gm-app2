package cn.gamemate.app.domain.arena.msg;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.msg.MsgArena.ArenaStart;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.clientmsg.MessageService;
import cn.gamemate.app.clientmsg.RelayServices;
import cn.gamemate.app.domain.arena.Arena;

public class ArenaStartMessage extends ClientMessage {

	@Override
	public int getCode() {
		return 0x230B;
	}
	
	private Arena arena;

	public ArenaStartMessage(Arena arena, proto.res.ResArena.Arena arenaSnapshot) {

		// if (p2pService == null){
		// throw new RuntimeException("can not initialize p2pservice.");
		// }
		arena.setUserIdList(receivers);
		this.arena = arena;
		rootBuilder.setArenaStart(ArenaStart.newBuilder()
				.setArena(arenaSnapshot)
				.setBattleID(arena.getLastBattle().getUuid().toString()));
	}

	public ArenaStartMessage(Arena arena) {
		// if (p2pService == null){
		// throw new RuntimeException("can not initialize p2pservice.");
		// }
		arena.setUserIdList(receivers);
		rootBuilder.setArenaStart(ArenaStart.newBuilder()
				.setArena(arena.toProtobuf())
				.setBattleID(UUID.randomUUID().toString()));
	}
	@Override
	public void send(){
		messageService.send(this);
		MessageService relayService = RelayServices.getRelayService(arena.getSlots().get(0).getUser());
		if (relayService !=null) relayService.send(this);

		
	}

}

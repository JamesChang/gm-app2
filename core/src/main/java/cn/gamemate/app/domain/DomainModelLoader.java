package cn.gamemate.app.domain;

import org.springframework.beans.factory.annotation.Autowired;

import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.clientmsg.NettyMessageService;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.DefaultArenaBuilder;
import cn.gamemate.app.domain.event.Arena05;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.user.User;

public class DomainModelLoader {
	

	public DomainModelLoader(NettyMessageService messageService){
		//ClientMessage.messageService = messageService;
		//System.out.println("==================="+ messageService);
		
	}
	
	
}

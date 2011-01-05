package cn.gamemate.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.safehaus.uuid.UUIDGenerator;

import cn.gamemate.app.clientmsg.MessageService;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.clientmsg.NettyMessageService;
import cn.gamemate.app.clientmsg.UserWhisperMessage;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.arena.DefaultArenaBuilder;
import cn.gamemate.app.domain.arena.msg.ArenaJoinedMessage;
import cn.gamemate.app.domain.arena.msg.ArenaStartMessage;
import cn.gamemate.app.domain.event.Arena05;
import cn.gamemate.app.domain.event.ArenaListEvent05;
import cn.gamemate.app.domain.event.DotaBattle;
import cn.gamemate.app.domain.event.EventCenter;
import cn.gamemate.app.domain.event.GreedyMatcher;
import cn.gamemate.app.domain.event.Ladder05;
import cn.gamemate.app.domain.event.ParsedDotaReplay;
import cn.gamemate.app.domain.event.ParsedDotaReplay.Chat;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.party.PartyManager;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;
import cn.gamemate.app.domain.user.User.UserStatus;
import cn.gamemate.app.security.DomainUserDetailsService;
import cn.gamemate.app.security.LatencyDbAuthenticationProvider;
import me.prettyprint.cassandra.dao.SimpleCassandraDao;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

public class JustRun {
	
	
	public static void main(String[] args) throws IOException {
		
		
		//System.setProperty("msgsrv.host", "192.168.110.198");
		
//		ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
//		
//		
////		EventCenter eventCenter = ctx.getBean(EventCenter.class);
////		ArenaListEvent05 event = eventCenter.getEvent(2);
//		User user1 = new User();
//		user1.setId(64);
//		user1.setName("user1");
//		user1.setPortrait("");
//		user1.setStatus(UserStatus.ONLINE);
//		User user2 = new User();
//		user2.setId(67);
//		user2.setName("user2");
//		user2.setPortrait("");
//		user2.setStatus(UserStatus.ONLINE);
//		UserRepository users = ctx.getBean(UserRepository.class);
//		users.login(user1);
//		users.login(user2);
//		
//		PartyManager partyManager = ctx.getBean(PartyManager.class);
//		ArrayList<User> arrayList = new ArrayList<User>();
//		arrayList.add(user2);
//		partyManager.userCreateParty(user1, arrayList);
//		
		
	}

}

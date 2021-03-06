package cn.gamemate.app.domain.event;

import java.lang.management.ManagementFactory;
import java.util.Arrays;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;

import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.BasicArenaRepository;
import cn.gamemate.app.domain.arena.DefaultArenaBuilder;
import cn.gamemate.app.domain.event.awards.AwardsPackage;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.AlertMessage;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserExtension;
import cn.gamemate.app.domain.user.UserRepository;

@Configuration
public class CampusArena05Configuration {
	
	@Autowired(required=true)
	private Game war3;
	
	@Autowired(required=true) 
	private Game dota;
	
	@Autowired(required=true)
	private Game sc;
	
	@Autowired(required=true)
	private UserRepository userRepository;
	

	@Bean public EventCenter eventCenter(){
		EventCenter ec = new EventCenter();
		return ec;
	}
	
	//@Autowired(required=true)
	AwardsPackage p1;
	
	//@Autowired(required=true)
	AwardsPackage p3;
	
	//@Autowired(required=true)
	AwardsPackage p2;
	
	//运行IMBA地图的时候，不能有裁判席
	public DefaultArenaBuilder dotaArenaBuilder(){
		return new DefaultArenaBuilder(){
		
			@Override
			public Integer getRefereeNum() {
				if (map != null && map.getName().toLowerCase().indexOf("imba") > 0){
					return 0;
				}
				else{
					return super.getRefereeNum();
				}
			}
		};
	}
	/*
	@Bean public SimpleHall defaultWar3ArenaListEvent(){
		
		SimpleHall event= new SimpleHall();
		event.setName("魔兽 自由赛");
		event.id = 1;
		event.game = war3;
		DefaultArenaBuilder war3Builder = new DefaultArenaBuilder();
		war3Builder
			.setGame(war3)
			.setGameVersion("1.24.1.6374");			
		event.gameMaps.add(GameMap.findGameMap(6L)); //TM
		event.gameMaps.add(GameMap.findGameMap(46L)); //TR
		event.gameMaps.add(GameMap.findGameMap(47L)); //LT
		event.gameMaps.add(GameMap.findGameMap(49L)); //EI
		event.setArenaBuilder(war3Builder);
		eventCenter().addEvent(1, event);
		return event;
	}
	
	@Bean public Hall defaultDotaArenaListEvent(){
		BigHall event =  new BigHall();
		event.id = 2;
		event.setName("DotA 自由赛");
		event.game = dota;
		event.setReplayRequired(true);
		DefaultArenaBuilder b = dotaArenaBuilder();
		b.setGameMap(87)
			.setGame(dota)
			.setGameVersion("1.24.1.6374")			
			.bisectSlots();
		
		event.gameMaps.add(GameMap.findGameMap(83L)); //6.71b
		event.gameMaps.add(GameMap.findGameMap(90L)); //6.72b
		event.gameMaps.add(GameMap.findGameMap(91L)); 
		event.gameMaps.add(GameMap.findGameMap(86L));
		event.setArenaBuilder(b);
		eventCenter().addEvent(2, event);
		event.start();
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName objectName = new ObjectName(
					"cn.gamemate.app:name=defaultDotaHall");
			mbs.registerMBean(event, objectName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return event;
	}
	
	@Bean public SimpleHall defaultScArenaListEvent(){
		SimpleHall event =  new SimpleHall();
		event.id = 3;
		event.setName("星际 自由赛");
		event.game = sc;
		DefaultArenaBuilder scBuilder = new DefaultArenaBuilder();
		scBuilder
			.setGame(sc)
			.setGameVersion("1.15.1.1");
		event.gameMaps.add(GameMap.findGameMap(41L));
		event.gameMaps.add(GameMap.findGameMap(42L));
		event.gameMaps.add(GameMap.findGameMap(43L));
		event.gameMaps.add(GameMap.findGameMap(44L));
		event.setArenaBuilder(scBuilder);
		eventCenter().addEvent(3, event);
		return event;
	}
	*/
/*
	@Bean public Hall evevt4(){
		BigHall event =  new BigHall();
		event.id = 4;
		event.setName("GameMate测试邀请赛");
		event.game = dota;
		DefaultArenaBuilder b = dotaArenaBuilder();
		b.setGameMap(87)
			.setGame(dota)
			.setGameVersion("1.24.1.6374")			
			.bisectSlots()
			.setRefereeNum(2);

		event.gameMaps.add(GameMap.findGameMap(83L));
		event.gameMaps.add(GameMap.findGameMap(87L));
		event.gameMaps.add(GameMap.findGameMap(86L));
		event.gameMaps.add(GameMap.findGameMap(88L));
		
		event.setArenaBuilder(b);
		eventCenter().addEvent(4, event);
		event.start();
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName objectName = new ObjectName(
					"cn.gamemate.app:name=event4");
			mbs.registerMBean(event, objectName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return event;
	}
	*/
/*
	@Bean public Hall event5(){
		BigHall event =  new BigHall();
		event.id = 5;
		event.setName("CA联赛");
		event.game = dota;
		DefaultArenaBuilder b = dotaArenaBuilder();
		b.setGameMap(87)
			.setGame(dota)
			.setGameVersion("1.24.1.6374")			
			.bisectSlots()
			.setRefereeNum(2);

		event.gameMaps.add(GameMap.findGameMap(83L));
		event.gameMaps.add(GameMap.findGameMap(87L));
		event.gameMaps.add(GameMap.findGameMap(86L));
		event.gameMaps.add(GameMap.findGameMap(88L));
		
		event.setArenaBuilder(b);
		eventCenter().addEvent(5, event);
		event.start();
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName objectName = new ObjectName(
					"cn.gamemate.app:name=event5");
			mbs.registerMBean(event, objectName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return event;
	}
	*/
/*
	@Bean public Hall event6(){
		BigHall event =  new BigHall();
		event.id = 6;
		event.setName("交大竞技赛");
		event.game = dota;
		event.setReplayRequired(true);
		DefaultArenaBuilder b = dotaArenaBuilder();
		b.setGameMap(87)
			.setGame(dota)
			.setGameVersion("1.24.1.6374")			
			.bisectSlots()
			.setRefereeNum(2);
		event.setAwardsPackage(Arrays.asList(p1, p2, p3));
		event.gameMaps.add(GameMap.findGameMap(83L)); //6.71b
		event.gameMaps.add(GameMap.findGameMap(90L)); //6.72b
		event.gameMaps.add(GameMap.findGameMap(91L)); 
		event.gameMaps.add(GameMap.findGameMap(86L));

		
		event.setArenaBuilder(b);
		eventCenter().addEvent(6, event);
		event.start();
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName objectName = new ObjectName(
					"cn.gamemate.app:name=event6");
			mbs.registerMBean(event, objectName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return event;
	}*/
	////////////////////////////////////////////
	// Ladder Events
	///////////////////////////////////////////
	/*
	//211
	@Bean 
        public Ladder06 ladder_war3_1v1(){
		Ladder06 ladder = new Ladder06();
		ladder.id = 211;
		int m = 1;
		ladder.m = m;
		ladder.mode = "1v1";
		ladder.name="war3 1v1";
		ladder.game = war3;
		DefaultArenaBuilder finalBuilder = new DefaultArenaBuilder();
		finalBuilder
			.setName("LadderGame")
			.setGame(war3)
			.setGameVersion("1.24.1.6374")
			.setSlotNum(m, m);
		ladder.finalBuilder =finalBuilder;
		ladder.setMatcher(new GreedyMatcher(m, userRepository));
		ladder.gameMaps.add(GameMap.findGameMap(6L)); //TM
		ladder.gameMaps.add(GameMap.findGameMap(46L)); //TR
		ladder.gameMaps.add(GameMap.findGameMap(47L)); //LT
		ladder.gameMaps.add(GameMap.findGameMap(49L)); //EI
		eventCenter().addLadder(211, ladder);
		ladder.start();
		return ladder;
	}
	
	
	// 212
	@Bean
	public Ladder06 ladder_war3_2v2() {
		Ladder06 ladder = new Ladder06();
		ladder.id = 212;
		int m = 2;
		ladder.m = m;
		ladder.mode = "2v2";
		ladder.name = "war3 2v2";
		ladder.game = war3;
		DefaultArenaBuilder finalBuilder = new DefaultArenaBuilder();
		finalBuilder.setName("LadderGame").setGame(war3)
				.setGameVersion("1.24.1.6374").setSlotNum(m, m);
		ladder.finalBuilder = finalBuilder;
		ladder.setMatcher(new GreedyMatcher(m, userRepository));
		ladder.gameMaps.add(GameMap.findGameMap(6L)); // TM
		ladder.gameMaps.add(GameMap.findGameMap(46L)); // TR
		ladder.gameMaps.add(GameMap.findGameMap(47L)); // LT
		eventCenter().addLadder(212, ladder);
		ladder.start();
		return ladder;

	}
	
	// 221
	@Bean
	public Ladder06 ladder_dota_ap() {
		Ladder06 ladder = new Ladder06();
		ladder.id = 221;
		int m = 2;
		ladder.m = m;
		ladder.mode = "ap";
		ladder.name = "DOTA 随机赛 -ap";
		ladder.game = dota;
		DefaultArenaBuilder finalBuilder = new DefaultArenaBuilder();
		finalBuilder.setName("LadderGame").setGame(dota)
				.setGameVersion("1.24.1.6374").setSlotNum(m, m);
		ladder.finalBuilder = finalBuilder;
		ladder.setMatcher(new GreedyMatcher(m, userRepository));
		ladder.gameMaps.add(GameMap.findGameMap(87L));
		eventCenter().addLadder(221, ladder);
		ladder.start();
		return ladder;

	}
	// 222
	@Bean
	public Ladder06 ladder_dota_rd() {
		Ladder06 ladder = new Ladder06();
		ladder.id = 222;
		int m = 5;
		ladder.m = m;
		ladder.mode = "rd";
		ladder.name = "DOTA 随机赛 -rd";
		ladder.game = dota;
		DefaultArenaBuilder finalBuilder = new DefaultArenaBuilder();
		finalBuilder.setName("LadderGame").setGame(dota)
				.setGameVersion("1.24.1.6374").setSlotNum(m, m);
		ladder.finalBuilder = finalBuilder;
		ladder.setMatcher(new GreedyMatcher(m, userRepository));
		ladder.gameMaps.add(GameMap.findGameMap(87L));
		eventCenter().addLadder(222, ladder);
		ladder.start();
		return ladder;

	}
	// 223
	@Bean
	public Ladder06 ladder_dota_cm() {
		Ladder06 ladder = new Ladder06();
		ladder.id = 221;
		int m = 5;
		ladder.m = m;
		ladder.mode = "cm";
		ladder.name = "DOTA 随机赛 -cm";
		ladder.game = dota;
		DefaultArenaBuilder finalBuilder = new DefaultArenaBuilder();
		finalBuilder.setName("LadderGame").setGame(dota)
				.setGameVersion("1.24.1.6374").setSlotNum(m, m);
		ladder.finalBuilder = finalBuilder;
		ladder.setMatcher(new GreedyMatcher(m, userRepository));
		ladder.gameMaps.add(GameMap.findGameMap(87L));
		eventCenter().addLadder(223, ladder);
		ladder.start();
		return ladder;

	}
	
	
	//231
	@Bean 
	public Ladder06 ladder_sc_1v1() {
		Ladder06 ladder = new Ladder06();
		ladder.id = 231;
		int m = 1;
		ladder.m = m;
		ladder.mode = "1v1";
		ladder.name = "星际争霸 1v1";
		ladder.game = sc;
		DefaultArenaBuilder finalBuilder = new DefaultArenaBuilder();
		finalBuilder.setName("LadderGame").setGame(sc)
				.setGameVersion("1.15.1.1").setSlotNum(m, m);
		ladder.finalBuilder = finalBuilder;
		ladder.setMatcher(new GreedyMatcher(m, userRepository));
		ladder.gameMaps.add(GameMap.findGameMap(41L));
		eventCenter().addLadder(231, ladder);
		ladder.start();
		return ladder;

	}
	
	//232
	@Bean 
	public Ladder06 ladder_sc_2v2() {
		Ladder06 ladder = new Ladder06();
		ladder.id = 232;
		int m = 2;
		ladder.m = m;
		ladder.mode = "2v2";
		ladder.name = "星际争霸 2v2";
		ladder.game = sc;
		DefaultArenaBuilder finalBuilder = new DefaultArenaBuilder();
		finalBuilder.setName("LadderGame").setGame(sc)
				.setGameVersion("1.15.1.1").setSlotNum(m, m);
		ladder.finalBuilder = finalBuilder;
		ladder.setMatcher(new GreedyMatcher(m, userRepository));
		ladder.gameMaps.add(GameMap.findGameMap(41L));
		eventCenter().addLadder(232, ladder);
		ladder.start();
		return ladder;

	}
	
	@Bean 
	public Ladder06 ladder_sc_3v3() {
		Ladder06 ladder = new Ladder06();
		ladder.id = 233;
		int m = 3;
		ladder.m = m;
		ladder.mode = "3v3";
		ladder.name = "星际争霸 2v3";
		ladder.game = sc;
		DefaultArenaBuilder finalBuilder = new DefaultArenaBuilder();
		finalBuilder.setName("LadderGame").setGame(sc)
				.setGameVersion("1.15.1.1").setSlotNum(m, m);
		ladder.finalBuilder = finalBuilder;
		ladder.setMatcher(new GreedyMatcher(m, userRepository));
		ladder.gameMaps.add(GameMap.findGameMap(42L));
		ladder.gameMaps.add(GameMap.findGameMap(43L));
		ladder.gameMaps.add(GameMap.findGameMap(44L));
		eventCenter().addLadder(233, ladder);
		ladder.start();
		return ladder;

	}
	
	@Bean
	public BasicArenaRepository<Integer> arenaRepository(){
		BasicArenaRepository<Integer> arenaRepository = new BasicArenaRepository<Integer>();
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName objectName = new ObjectName(
					"cn.gamemate.app:name=arenaRepository");
			mbs.registerMBean(arenaRepository, objectName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return arenaRepository;
	}*/

	
	@Bean
	public UserExtension arena05UserExtension(){
		
		UserExtension ux = new UserExtension(){
			
			@Override
			public void userLoggedOut(User user) {
				super.userLoggedOut(user);
				clearArena(user);
			}
			private void clearArena(User user) {
				if (user.getArenaId()== null){
					return;
				}
				Arena arena = Arena.findArena(user.getArenaId());
				if (arena != null && arena instanceof Arena05){
					Arena05 arena05 = (Arena05)arena;
					arena05._userLeave(user);
				}
			}
			@Override
			public void userDrop(User user) {
				super.userLoggedOut(user);
				clearArena(user);
			}
			
			@Override
			public void userBrowseOnly(User user) {
				super.userBrowseOnly(user);
				clearArena(user);
			}
			
		};
		userRepository.addExtension(ux);
		return ux;
	}
	
	
	
	

}

package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;
import cn.gamemate.app.domain.event.PowerBasedLadderMatcher;
import cn.gamemate.app.domain.event.PowerBasedLadderMatcher.SpaceNode;

@Configuration
class PowerBasedLadderMatcherTestConfig{
	public @Bean UserRepository userRepository(){
		UserRepository users = new UserRepository();
		users.login(new User(1,"user1", "http://"));
		users.login(new User(2,"user2", "http://"));
		users.login(new User(3,"user3", "http://"));
		users.login(new User(4,"user4", "http://"));
		users.login(new User(5,"user5", "http://"));
		users.login(new User(6,"user6", "http://"));
		users.login(new User(7,"user7", "http://"));
		users.login(new User(8,"user8", "http://"));
		users.login(new User(9,"user9", "http://"));
		users.login(new User(10,"user10", "http://"));
		return users;
	}
	
	public @Bean PowerBasedLadderMatcher matching1v1(){
		return new PowerBasedLadderMatcher(1, userRepository());
	}
	
	
	public PowerBasedLadderMatcher matching2v2(){
		return new PowerBasedLadderMatcher(2, userRepository());
	}
	
	public PowerBasedLadderMatcher matching5v5(){
		return new PowerBasedLadderMatcher(5, userRepository());
	}
	
	
}

public class PowerBasedLadderMatcherTest{
	
	private ApplicationContext ctx;
	private List<MatcherContext> matchings= new ArrayList<MatcherContext>();
	private UserRepository users;
	
	private void assertMatching(MatcherContext matching){
		for (Party p1: matching.getForce1Paries()){
			for (Party p2: matching.getForce2Paries()){
				if (p1 == p2)
					Assert.fail("the same party in two force");
			}
		}
	}
	
	private void assertMatching(Party[] force1,MatcherContext matching){
		//assert force1 must be in one side 
		assertMatching(matching);
		List<Party> target;
		Party p = force1[0];
		if (!matching.getForce1Paries().contains(p)){
			target = matching.getForce2Paries();
		}else{
			target = matching.getForce2Paries();
		}
		
		Assert.assertEquals(force1.length, target.size());
		for (Party p1: force1){
			Assert.assertTrue(target.contains(p1));
		}
		
		 
	}
	

	@Before
	public void setUp(){
		ctx = new AnnotationConfigApplicationContext(PowerBasedLadderMatcherTestConfig.class);
		users = ctx.getBean("userRepository", UserRepository.class);
		matchings.clear();
	}
	
	@Test
	public void testDoubleLinkedList(){
		PowerBasedLadderMatcher.SpaceNode head = new SpaceNode();
		PowerBasedLadderMatcher.SpaceNode node1 = new SpaceNode();
		PowerBasedLadderMatcher.SpaceNode node2 = new SpaceNode();
		PowerBasedLadderMatcher.SpaceNode node3 = new SpaceNode();
		PowerBasedLadderMatcher.SpaceNode node4 = new SpaceNode();
		node1.power = 100;
		node2.power = 120;
		node3.power = 110;
		node4.power = 90;
		head.add(node1);
		head.add(node2);
		head.add(node3);
		head.add(node4);
		//90, 100, 110, 120
		Assert.assertNull(head.prev);
		Assert.assertEquals(node4, head.next);
		Assert.assertEquals(node1, head.next.next);
		Assert.assertEquals(node3, head.next.next.next);
		Assert.assertEquals(node2, head.next.next.next.next);
		Assert.assertNull(head.next.next.next.next.next);
		
		node3.power = 200;
		node3.update();
		//90 100 120 200
		Assert.assertNull(head.prev);
		Assert.assertEquals(node4, head.next);
		Assert.assertEquals(node1, head.next.next);
		Assert.assertEquals(node2, head.next.next.next);
		Assert.assertEquals(node3, head.next.next.next.next);
		Assert.assertNull(head.next.next.next.next.next);
		
		node2.power = 80;
		node2.update();
		//80,90, 100, 200
		Assert.assertNull(head.prev);
		Assert.assertEquals(node2, head.next);
		Assert.assertEquals(node4, head.next.next);
		Assert.assertEquals(node1, head.next.next.next);
		Assert.assertEquals(node3, head.next.next.next.next);
		Assert.assertNull(head.next.next.next.next.next);
		
		node1.power = 130;
		node1.update();
		//80,90, 130, 200
		Assert.assertNull(head.prev);
		Assert.assertEquals(node2, head.next);
		Assert.assertEquals(node4, head.next.next);
		Assert.assertEquals(node1, head.next.next.next);
		Assert.assertEquals(node3, head.next.next.next.next);
		Assert.assertNull(head.next.next.next.next.next);
		
		node2.power = 230;
		node2.update();
		//90, 130, 200, 230
		Assert.assertNull(head.prev);
		Assert.assertEquals(node4, head.next);
		Assert.assertEquals(node1, head.next.next);
		Assert.assertEquals(node3, head.next.next.next);
		Assert.assertEquals(node2, head.next.next.next.next);
		Assert.assertNull(head.next.next.next.next.next);
		
	}
	
	@Test()
	public void test1v1_1_1() throws InterruptedException{
		LadderMatcher matching = ctx.getBean("matching1v1", LadderMatcher.class);
		matching.addHandler(new MatcherHandler(){
			@Override
			public void onMatched(MatcherContext context) {
				matchings.add(context);
			}
		});
		Party team1 = new TeamMock(users.getUser(1));
		Party team2 = new TeamMock(users.getUser(2));
		
		matching.add(team1);
		matching.add(team2);
		
		matching.tick();
		Thread.sleep(5000);
		matching.tick();
		//System.out.println(matchings);
		Assert.assertEquals(1,matchings.size());
		//assertMatching(new Party[]{team1}, new Party[]{team2}, matchings.get(0));
		
	}
	
}
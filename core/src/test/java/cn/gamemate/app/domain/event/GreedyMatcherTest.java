package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;

@Configuration
class GreedyMatcherTestConfig{
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
	
	public @Bean GreedyMatcher matching1v1(){
		return new GreedyMatcher(1, userRepository());
	}
	
	public @Bean GreedyMatcher matching2v2(){
		return new GreedyMatcher(2, userRepository());
	}
	
	public @Bean GreedyMatcher matching5v5(){
		return new GreedyMatcher(5, userRepository());
	}
	
	
	
}

class TeamMock implements Party{
	protected List<User> users = new ArrayList<User>();
	
	public TeamMock() {
		// TODO Auto-generated constructor stub
	}
	
	public TeamMock(User user) {
		addPlayer(user);
	}
	
	public TeamMock(User[] users) {
		for (User u:users){
			addPlayer(u);
		}
		
	}
	
	public void addPlayer(User user){
		users.add(user);
	}

	@Override
	public int getPlayerCount() {
		return users.size();
	}
	
	@Override
	public String toString() {
		ToStringHelper stringHelper = Objects.toStringHelper(this);
		for(User u:users){
			stringHelper.addValue(u);
		}
		return stringHelper.toString();
		
	}

}

public class GreedyMatcherTest {
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
	
	private void assertMatching(Party[] force1, Party[] force2, MatcherContext matching){
		//assert no intersection
		assertMatching(matching);
		Party p = force1[0];
		if (!matching.getForce1Paries().contains(p)){
			Party[] t = force1;
			force1 = force2;
			force2 = t;
		}
		
		Assert.assertEquals(force1.length, matching.force1Parties.size());
		Assert.assertEquals(force2.length, matching.force2Parties.size());
		for (Party p1: force1){
			Assert.assertTrue(matching.force1Parties.contains(p1));
		}
		for (Party p2: force2){
			
			Assert.assertTrue(matching.force2Parties.contains(p2));
		}
		 
	}
	
	
	@Before
	public void setUp(){
		ctx = new AnnotationConfigApplicationContext(GreedyMatcherTestConfig.class);
		users = ctx.getBean("userRepository", UserRepository.class);
		matchings.clear();
	}
	
	@Test(timeout=6000)
	public void test1v1_1_1() throws InterruptedException{
		GreedyMatcher matching = ctx.getBean("matching1v1", GreedyMatcher.class);
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
		//assert matchings.size() == 1;
		assertMatching(new Party[]{team1}, new Party[]{team2}, matchings.get(0));
		
	}
	

	@Test(timeout=6000)
	public void test1v1_1_1_1_1() throws InterruptedException{
		GreedyMatcher matching = ctx.getBean("matching1v1", GreedyMatcher.class);
		matching.addHandler(new MatcherHandler(){
			@Override
			public void onMatched(MatcherContext context) {
				matchings.add(context);
			}
		});
		Party team1 = new TeamMock(users.getUser(1));
		Party team2 = new TeamMock(users.getUser(2));
		Party team3 = new TeamMock(users.getUser(3));
		Party team4 = new TeamMock(users.getUser(4));
		
		matching.add(team1);
		matching.add(team2);
		matching.add(team3);
		matching.add(team4);
		matching.tick();
		Thread.sleep(5000);
		matching.tick();
		Assert.assertEquals(2,matchings.size());
	}
	

	@Test(timeout=11000)
	public void test1v1_1_1_1_t_1() throws InterruptedException{
		GreedyMatcher matching = ctx.getBean("matching1v1", GreedyMatcher.class);
		matching.addHandler(new MatcherHandler(){
			@Override
			public void onMatched(MatcherContext context) {
				matchings.add(context);
			}
		});
		Party team1 = new TeamMock(users.getUser(1));
		Party team2 = new TeamMock(users.getUser(2));
		Party team3 = new TeamMock(users.getUser(3));
		Party team4 = new TeamMock(users.getUser(4));
		matching.add(team1);
		matching.add(team2);
		matching.add(team3);
		matching.tick();
		Thread.sleep(5000);
		matching.tick();
		
		Assert.assertEquals(1,matchings.size());
		matchings.clear();
		
		matching.add(team4);
		matching.tick();
		Thread.sleep(5000);
		matching.tick();
		Assert.assertEquals(1,matchings.size());
	}
	
	@Test(timeout=6000)
	public void test2v2_1_2_1() throws InterruptedException{
		GreedyMatcher matching = ctx.getBean("matching2v2", GreedyMatcher.class);
		matching.addHandler(new MatcherHandler(){
			@Override
			public void onMatched(MatcherContext context) {
				matchings.add(context);
			}
		});
		Party team1 = new TeamMock(users.getUser(1));
		Party team2 = new TeamMock(new User[]{users.getUser(2), users.getUser(3)});
		Party team3 = new TeamMock(users.getUser(4));
		matching.add(team1);
		matching.add(team2);
		matching.add(team3);
		
		matching.tick();
		Thread.sleep(5000);
		matching.tick();
		Assert.assertEquals(1,matchings.size());
		assertMatching(new Party[]{team1, team3}, new Party[]{team2}, matchings.get(0));
		
	}
	
	@Test(timeout=6000)
	public void test2v2_2_2() throws InterruptedException{
		GreedyMatcher matching = ctx.getBean("matching2v2", GreedyMatcher.class);
		matching.addHandler(new MatcherHandler(){
			@Override
			public void onMatched(MatcherContext context) {
				matchings.add(context);
			}
		});
		Party team1 = new TeamMock(new User[]{users.getUser(1), users.getUser(4)});
		Party team2 = new TeamMock(new User[]{users.getUser(2), users.getUser(3)});
		matching.add(team1);
		matching.add(team2);
		
		matching.tick();
		Thread.sleep(5000);
		matching.tick();
		Assert.assertEquals(1,matchings.size());
		assertMatching(new Party[]{team1}, new Party[]{team2}, matchings.get(0));
	}
	
	@Test(timeout=6000)
	public void test2v2_1_1_1_2() throws InterruptedException{
		GreedyMatcher matching = ctx.getBean("matching2v2", GreedyMatcher.class);
		matching.addHandler(new MatcherHandler(){
			@Override
			public void onMatched(MatcherContext context) {
				matchings.add(context);
			}
		});
		Party team1 = new TeamMock(users.getUser(1));
		Party team2 = new TeamMock(users.getUser(2));
		Party team3 = new TeamMock(new User[]{users.getUser(3), users.getUser(4)});
		Party team4 = new TeamMock(users.getUser(5));
		matching.add(team1);
		matching.add(team2);
		matching.add(team4);
		matching.add(team3);
		
		matching.tick();
		Thread.sleep(5000);
		matching.tick();
		Assert.assertEquals(1,matchings.size());
		
	}
	

	@Test(timeout=6000)
	public void test5v5() throws InterruptedException{
		GreedyMatcher matching = ctx.getBean("matching5v5", GreedyMatcher.class);
		matching.addHandler(new MatcherHandler(){
			@Override
			public void onMatched(MatcherContext context) {
				matchings.add(context);
			}
		});
		Party team1 = new TeamMock(users.getUser(1));
		Party team2 = new TeamMock(users.getUser(2));
		Party team3 = new TeamMock(new User[]{users.getUser(3), users.getUser(4)});
		Party team4 = new TeamMock(users.getUser(5));
		Party team5 = new TeamMock(new User[]{users.getUser(6), users.getUser(7),users.getUser(8),users.getUser(9),users.getUser(10)});
		matching.add(team1);
		matching.add(team2);
		matching.add(team5);
		matching.add(team4);
		matching.add(team3);
		
		matching.tick();
		Thread.sleep(5000);
		matching.tick();
		Assert.assertEquals(1,matchings.size());
		assertMatching(new Party[]{team5}, matchings.get(0));
	}
	


}


package cn.gamemate.app.domain.event.rts;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import proto.response.ResCampusArena.CA078_Leader_Board.Builder;

import com.google.common.collect.Maps;

import cn.gamemate.app.domain.DomainModelView;
import cn.gamemate.app.domain.event.rts.RoundRobin.LeaderBoard;
import cn.gamemate.app.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/applicationContext.xml"})
//@TransactionConfiguration(defaultRollback=false)
@Transactional
public class RoundRobinTest {

	private void userRegister(Integer userId, PlayerRegisterationSupport event){
		User user = User.findUser(userId);
		event.playerRegister(user, null);
	}
	private void plusplus(Map<String, Integer> counts, String key){
		Integer c = counts.get(key);
		if (c==null){c =0;}
		counts.put(key, c+1);
	}
	
	@Test
	public void test4Players(){
		
		RoundRobin tournament = new RoundRobin();
		tournament.setName("");
		tournament.setGameId(2);
		
		userRegister(64, tournament);
		userRegister(67, tournament);
		userRegister(70, tournament);
		userRegister(73, tournament);
		
		tournament.persist();
		tournament.generateChildren();
		
		Assert.assertEquals(6, tournament.getChildren().size());
		Map<String, Integer> counts = Maps.newHashMap();
		for (RtsEliminationMatch match:tournament.getChildren()){
			String name = match.getHome().getName();
			plusplus(counts, name);
			name = match.getAway().getName();
			plusplus(counts, name);
		}
		for (Integer v:counts.values()){
			Assert.assertEquals(3,(int)v);
		}
		
	}
	
	@Test
	public void test5Players(){
		
		RoundRobin tournament = new RoundRobin();
		tournament.setName("");
		tournament.setGameId(2);
		
		userRegister(64, tournament);
		userRegister(67, tournament);
		userRegister(70, tournament);
		userRegister(73, tournament);
		userRegister(76, tournament);
		
		tournament.persist();
		tournament.generateChildren();
		
		Assert.assertEquals(10, tournament.getChildren().size());
		Map<String, Integer> counts = Maps.newHashMap();
		for (RtsEliminationMatch match:tournament.getChildren()){
			String name = match.getHome().getName();
			plusplus(counts, name);
			name = match.getAway().getName();
			plusplus(counts, name);
		}
		for (Integer v:counts.values()){
			Assert.assertEquals(4,(int)v);
		}
		
	}
    
	@Test
	public void testWinOf2Players(){
		RoundRobin tournament = new RoundRobin();
		tournament.setGameId(2);
		tournament.setName("");
		
		User user1 = User.findArenaUserByName("user1");
		User user2 = User.findArenaUserByName("user2");
		tournament.playerRegister(user1, null);
		tournament.playerRegister(user2, null);
		
		tournament.persist();
		tournament.generateChildren();
		Assert.assertEquals(1, tournament.getChildren().size());
		
		tournament.getChildren().get(0).playerSubmitResult(
				user1, null, 0, 0, null);
		Assert.assertEquals(RtsEliminationMatch.Status.FINISHED,
				tournament.getChildren().get(0).getStatus());
		
		LeaderBoard leaderBoad = tournament.getLeaderboad(null);
		Builder builder = leaderBoad.toProtobuf();
		Assert.assertEquals(2, builder.getItemsCount());
		Assert.assertEquals(3, builder.getItems(0).getRtsScore());
		Assert.assertEquals("user1", builder.getItems(0).getName());
		System.out.println(builder.build());
		
	}
	
	//@Test
	public void testDrawOf2Players(){
		
		//RO2, 310
		RoundRobin tournament = new RoundRobin();
		tournament.setGameId(2);
		tournament.setName("");
		tournament.setDefaultMaxRound(2);
		tournament.setDefaultRequiredWin(2);
		
		User user1 = User.findArenaUserByName("user1");
		User user2 = User.findArenaUserByName("user2");
		tournament.playerRegister(user1, null);
		tournament.playerRegister(user2, null);
		
		tournament.persist();
		tournament.generateChildren();
		Assert.assertEquals(1, tournament.getChildren().size());
		
		tournament.getChildren().get(0).playerSubmitResult(
				user1, null, 0, 0, null);

		Assert.assertEquals(RtsEliminationMatch.Status.READY,
				tournament.getChildren().get(0).getStatus());
		tournament.getChildren().get(0).playerSubmitResult(
				user1, null, 1, 1, null);
		Assert.assertEquals(RtsEliminationMatch.Status.FINISHED,
				tournament.getChildren().get(0).getStatus());
		
		LeaderBoard leaderBoad = tournament.getLeaderboad(null);
		Builder builder = leaderBoad.toProtobuf();
		Assert.assertEquals(2, builder.getItemsCount());
		Assert.assertEquals(1, builder.getItems(0).getRtsScore());
		Assert.assertEquals(1, builder.getItems(1).getRtsScore());
		System.out.println(builder.build());
		
	}
	

	//@Test
	public void testLossOf2Players(){
		RoundRobin tournament = new RoundRobin();
		tournament.setGameId(2);
		tournament.setName("");
		
		User user1 = User.findArenaUserByName("user1");
		User user2 = User.findArenaUserByName("user2");
		tournament.playerRegister(user1, null);
		tournament.playerRegister(user2, null);
		
		tournament.persist();
		tournament.generateChildren();
		Assert.assertEquals(1, tournament.getChildren().size());
		
		tournament.getChildren().get(0).playerSubmitResult(
				user1, null, 0, 1, null);
		Assert.assertEquals(RtsEliminationMatch.Status.FINISHED,
				tournament.getChildren().get(0).getStatus());
		
		LeaderBoard leaderBoad = tournament.getLeaderboad(null);
		Builder builder = leaderBoad.toProtobuf();
		Assert.assertEquals(1, builder.getItemsCount());
		Assert.assertEquals(3, builder.getItems(0).getRtsScore());
		Assert.assertEquals("user2", builder.getItems(0).getName());
		System.out.println(builder.build());
		
	}
	
	//@Test
	public void testReload(){
		RoundRobin tournament = RoundRobin.findRoundRobin(2742);
		User user1 = User.findArenaUserByName("user1");
		DomainModelView view = tournament.newView(user1);
		System.out.println(view.toProtobuf().build());
		
		DomainModelView view2 = tournament.newView(user1);
		System.out.println(view2.toProtobuf().build());
		
	}
	

	//@Test
	public void test4_Players(){
		
		//RO2, 310
		RoundRobin tournament = new RoundRobin();
		tournament.setGameId(2);
		tournament.setName("");
		tournament.setDefaultRequiredWin(2);
		
		
		User user1 = User.findArenaUserByName("user1");
		User user2 = User.findArenaUserByName("user2");
		User user3 = User.findArenaUserByName("user3");
		User user4 = User.findArenaUserByName("user4");
		tournament.playerRegister(user1, null);
		tournament.playerRegister(user2, null);
		tournament.playerRegister(user3, null);
		tournament.playerRegister(user4, null);
		tournament.getAdministrators().add(user1);
		
		tournament.persist();
		tournament.generateChildren();
		Assert.assertEquals(6, tournament.getChildren().size());
		
		/*tournament.getChildren().get(0).playerSubmitResult(
				user1, null, 0, 0, null);
		tournament.getChildren().get(1).playerSubmitResult(
				user1, null, 0, 0, null);
		tournament.getChildren().get(2).playerSubmitResult(
				user1, null, 0, 0, null);
		tournament.getChildren().get(3).playerSubmitResult(
				user1, null, 0, 0, null);
		tournament.getChildren().get(4).playerSubmitResult(
				user1, null, 0, 0, null);
		tournament.getChildren().get(5).playerSubmitResult(
				user1, null, 0, 0, null);*/
		
		com.google.protobuf.GeneratedMessage.Builder builder = tournament.newView(user1).toProtobuf();
		
		//LeaderBoard leaderBoad = tournament.getLeaderboad(null);
		//Builder builder = leaderBoad.toProtobuf();
		//Assert.assertEquals(6, builder.getItemsCount());
		/*Assert.assertEquals(1, builder.getItems(0).getRtsScore());
		Assert.assertEquals(1, builder.getItems(1).getRtsScore());*/
		System.out.println(builder.build());
		
	}
	
	
	//@Test
	public void testGroupElimination(){
		
		GroupElimination tournament = new GroupElimination();
		tournament.setGameId(2);
		tournament.setName("");
		tournament.persist();
		
		
		User user1 = User.findArenaUserByName("user1");
		
		RoundRobin g1 = RoundRobin.findRoundRobin(2782);
		tournament.getChildren().add(g1);
		/*RoundRobin g2 = RoundRobin.findRoundRobin(2687);
		tournament.getChildren().add(g2);*/
		tournament.merge();
		
		DomainModelView view = tournament.newView(user1);
		System.out.println(view.toProtobuf().build());
		
		
	}
	
	//@Test
	public void testWinOf2Teams(){
		RoundRobin tournament = new RoundRobin();
		tournament.setGameId(2);
		tournament.setName("");
		tournament.setEventForceType(RtsEventForceType.TEAM);
		
		User user1 = User.findArenaUserByName("user1");
		User user2 = User.findArenaUserByName("user2");
		RtsTeam team1 = RtsTeam.findRtsTeam("77cff450-be3f-4848-ba7c-b2b030ccb49b");
		RtsTeam team2 = RtsTeam.findRtsTeam("96592935-c20b-4a1b-a8f5-204245ea325d");
		tournament.playerRegister(user1, team1);
		tournament.playerRegister(user2, team2);
		
		tournament.persist();
		tournament.generateChildren();
		Assert.assertEquals(1, tournament.getChildren().size());
		
		tournament.getChildren().get(0).playerSubmitResult(
				user1, team1, 0, 0, null);
		Assert.assertEquals(RtsEliminationMatch.Status.FINISHED,
				tournament.getChildren().get(0).getStatus());
		
		LeaderBoard leaderBoad = tournament.getLeaderboad(null);
		Builder builder = leaderBoad.toProtobuf();
		Assert.assertEquals(2, builder.getItemsCount());
		Assert.assertEquals(3, builder.getItems(0).getRtsScore());
		//Assert.assertEquals("user1", builder.getItems(0).getName());
		System.out.println(builder.build());
		
	}
	
	
	//- Fix group310 bug
	
	//@Test
	public void testBug(){
		RtsEliminationMatch match = RtsEliminationMatch.findRtsEliminationMatch(2791);
		User user2 = User.findArenaUserByName("user2");
		RtsTeam team2 = RtsTeam.findRtsTeam("96592935-c20b-4a1b-a8f5-204245ea325d");
		match.playerSubmitResult(
				user2, team2, 0, 0, null);
		
	}
	
	//- Grouping Test
	
	@Test
	public void testAddGroup(){
		GroupElimination tournament = new GroupElimination();
		tournament.setGameId(2);
		tournament.setName("");
		tournament.setEventForceType(RtsEventForceType.USER);
		tournament.setDefaultMaxRound(2);
		tournament.setDefaultRequiredWin(2);
		
		User user1 = User.findArenaUserByName("user1");
		User user2 = User.findArenaUserByName("user2");
		User user3 = User.findArenaUserByName("user3");
		User user4 = User.findArenaUserByName("user4");
		tournament.administrators.add(user1);
		tournament.playerRegister(user1, null);
		tournament.playerRegister(user2, null);
		tournament.playerRegister(user3, null);
		tournament.playerRegister(user4, null);
		tournament.persist();
		Assert.assertEquals(4, tournament.registeredForces.size());
		
		RoundRobin groupA = tournament.newChildGroup(user1, "Group A");
		tournament.merge();
		Assert.assertEquals(1, tournament.children.size());
		
		RoundRobin groupB = tournament.newChildGroup(user1, "Group B");
		tournament.merge();
		Assert.assertEquals(2, tournament.children.size());
		
		tournament.deleteChildGroup(user1, "Group B");
		Assert.assertEquals(1, tournament.children.size());
		Assert.assertEquals("Group A", tournament.children.get(0).getName());
		
		tournament.moveForceToGroup(user1, 
				tournament.registeredForces.get(0).getId(), "Group A");
		Assert.assertEquals(1, groupA.registeredForces.size());
		
		tournament.moveForceToGroup(user1, 
				tournament.registeredForces.get(1).getId(), "Group A");
		Assert.assertEquals(2, groupA.registeredForces.size());
		
		tournament.moveForceToGroup(user1, 
				tournament.registeredForces.get(2).getId(), "Group A");
		Assert.assertEquals(3, groupA.registeredForces.size());
		
		tournament.moveForceToGroup(user1, 
				tournament.registeredForces.get(1).getId(), null);
		Assert.assertEquals(2, groupA.registeredForces.size());
		
		tournament.generateChildren();
		Assert.assertEquals(1, groupA.children.size());
		
		
		DomainModelView view = tournament.newView(user1);
		System.out.println(view.toProtobuf().build());
		
	}
	
	
	//@Test
	public void createTeamGroupElimination(){
		
		RoundRobin tournament = new RoundRobin();
		tournament.setGameId(2);
		tournament.setName("");
		tournament.setEventForceType(RtsEventForceType.TEAM);
		tournament.setDefaultMaxRound(2);
		tournament.setDefaultRequiredWin(2);
		
		User user1 = User.findArenaUserByName("user1");
		User user2 = User.findArenaUserByName("user2");
		User user3 = User.findArenaUserByName("user3");
		User user4 = User.findArenaUserByName("user4");
		RtsTeam team1 = RtsTeam.findRtsTeamByUserGame(user1, 2);
		RtsTeam team2 = RtsTeam.findRtsTeamByUserGame(user2, 2);
		RtsTeam team3 = RtsTeam.findRtsTeamByUserGame(user3, 2);
		RtsTeam team4 = RtsTeam.findRtsTeamByUserGame(user4, 2);
		tournament.playerRegister(user1, team1);
		tournament.playerRegister(user2, team2);
		tournament.playerRegister(user3, team3);
		tournament.playerRegister(user4, team4);
		
		tournament.persist();
		tournament.generateChildren();
		
		
		
		GroupElimination big = new GroupElimination();
		big.setGameId(2);
		big.setName("");
		big.setEventForceType(RtsEventForceType.TEAM);
		big.persist();
		
		
		
		RoundRobin g1 = tournament;
		big.getChildren().add(g1);
		/*RoundRobin g2 = RoundRobin.findRoundRobin(2687);
		tournament.getChildren().add(g2);*/
		big.merge();
		
		DomainModelView view = big.newView(user1);
		System.out.println(view.toProtobuf().build());
		
		
	}
}

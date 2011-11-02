package cn.gamemate.app.domain.event.rts;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.gamemate.app.domain.event.RegistrationTests;
import cn.gamemate.app.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/rdbms.xml"})
public class RtsEliminationTest {
	
	static User user1;
	static User user2;
	static User user3;
	static User user4;
	
	public RtsElimination new4UserElim(){
		RtsElimination elimination = new RtsElimination();
		elimination.setName("hi");
		elimination.setGameId(2);
		elimination.persist();
		user1 = User.findArenaUserByName("user1");
		user2 = User.findArenaUserByName("user2");
		user3 = User.findArenaUserByName("user3");
		user4 = User.findArenaUserByName("user4");
		elimination.administrators.add(user1);
		elimination.playerRegister(user1,null);
		elimination.playerRegister(user2,null);
		elimination.playerRegister(user3,null);
		elimination.playerRegister(user4,null);
		elimination.generateChildren();
		elimination.merge();
		return elimination;
	}
	
	//- 弃权/取消资格 --------------------------------------------
	
	@Test
	public void OneQuitOnReady(){
		RtsElimination elimination = new4UserElim();
		
		RtsEliminationMatch match1 = elimination.children.get(0);
		RtsEliminationMatch match2 = elimination.children.get(1);
		RtsEliminationMatch match3 = elimination.children.get(2);
		Assert.assertEquals(RtsEliminationMatch.Status.READY, match1.getStatus());
		
		elimination.unregister((RtsEventForce)(match1.getHome()));
		Assert.assertEquals(RtsEliminationMatch.Status.FINISHED, match1.getStatus());
		Assert.assertEquals(RtsEliminationMatch.Status.OPEN, match3.getStatus());
		Assert.assertEquals(match1.getAway(), match3.getHome());
		Assert.assertEquals(null, match3.getAway());
		
		elimination.newView(user1).toProtobuf();
	}
	
	@Test
	public void AllQuitOnReady(){
		RtsElimination elimination = new4UserElim();
		
		RtsEliminationMatch match1 = elimination.children.get(0);
		RtsEliminationMatch match2 = elimination.children.get(1);
		RtsEliminationMatch match3 = elimination.children.get(2);
		Assert.assertEquals(RtsEliminationMatch.Status.READY, match1.getStatus());
		
		elimination.unregister((RtsEventForce)(match1.getHome()));
		elimination.unregister((RtsEventForce)(match1.getAway()));
		Assert.assertEquals(RtsEliminationMatch.Status.FINISHED, match1.getStatus());
		Assert.assertEquals(RtsEliminationMatch.Status.OPEN, match3.getStatus());
		Assert.assertEquals(RtsEventForce.getQuitForce(), match3.getHome());
		Assert.assertEquals(null, match3.getAway());
		
		match2.playerSubmitResult(user1, null, 0, 0, null);
		Assert.assertEquals(RtsEliminationMatch.Status.FINISHED, match2.getStatus());
		Assert.assertEquals(RtsEliminationMatch.Status.FINISHED, match3.getStatus());
		Assert.assertEquals(match2.getHome(), match3.getWinnerForce());
		
		
		elimination.newView(user1).toProtobuf();
	}
	
	
	
	@Test
	public void testPlayerRegistrationSupport(){
		RtsElimination elimination = new RtsElimination();
		elimination.setName("hi");
		elimination.setGameId(2);
		elimination.persist();
		User user1 = User.findArenaUserByName("user1");
		elimination.administrators.add(user1);
		new RegistrationTests().runAllTests(elimination);
	}
	
	
}

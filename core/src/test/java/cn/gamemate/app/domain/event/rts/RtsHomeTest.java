package cn.gamemate.app.domain.event.rts;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cn.gamemate.app.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/applicationContext.xml"})
@TransactionConfiguration(defaultRollback=false)
@Transactional
public class RtsHomeTest {
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
	@Test
	public void testCreation(){
		//new
		RtsHome home = new RtsHome();
		home.setName("RTS Test Home");
		//add children
		RtsElimination match =new4UserElim();
		
		
		
		home.children.add(match);
		
		home.persist();

		//reload
		RtsHome reloaded = RtsHome.findRtsHome(home.getId());
		String output = reloaded.toProtobuf().toString();
		
	}
	
	//@Test
	public void testLoad(){
		RtsHome home = RtsHome.findRtsHome(2434);
		List<? extends RtsElimination> children = home.getChildren();
	}
	
}

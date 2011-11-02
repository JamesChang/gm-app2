package cn.gamemate.app.domain.event.rts;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import proto.response.ResEvent.EventGet.Builder;

import cn.gamemate.app.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/rdbms.xml"})
//@TransactionConfiguration(defaultRollback=false)
@Transactional
public class EventRegistrationTest {

	/*@Test
	public void testGenerateChildren2(){
		
		User user1  = User.findUser(64);
		RtsElimination match = new RtsElimination();
		
		
		match.setName("test elimination");
		match.setEventForceType(RtsEventForceType.USER);
		match.setGameId(2);
		match.administrators.add(user1);
		
		match.persist();
	}*/
	
	@Test
	public void testListRequiredField(){
		//TODO add fixtures
		RtsElimination elimination = RtsElimination.findRtsElimination(2458);
		User user1  = User.findUser(64);
		User user2  = User.findUser(67);
	//	elimination.playerRegister(user2, null);
		elimination.newRegistrationManagementView(user1);
		//System.out.println(view.build());
		//TODO: assert

	}
	
	@Test
	public void testSuccessReg(){
		//TODO add fixtures
		RtsElimination elimination = RtsElimination.findRtsElimination(2458);
		User user1  = User.findUser(64);
		User user3  = User.findUser(70);
		elimination.playerRegister(user3, null);

		//TODO: assert

	}
	
	@Test
	public void testFailedReg(){
		//TODO add fixtures
		RtsElimination elimination = RtsElimination.findRtsElimination(2458);
		User user6  = User.findUser(79);
		try{
			elimination.playerRegister(user6, null);
			Assert.fail("UserDetailedInfoRequired Exception expected.");
		}catch(UserDetailedInfoRequired e){
			Assert.assertTrue(e.getData().contains("mobile"));
		}
		 
		//TODO: assert

	}
	
}

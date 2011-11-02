package cn.gamemate.app.domain.event.rts;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.gamemate.app.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/rdbms.xml"})
//@TransactionConfiguration(defaultRollback=false)
@Transactional
public class DoubleEliminationTest {
	
	private void userRegister(Integer userId, PlayerRegisterationSupport event){
		User user = User.findUser(userId);
		event.playerRegister(user, null);
	}
	
	@Test
	public void testListRequiredField(){
		
		DoubleElimination elimination = new DoubleElimination();
		
		userRegister(64, elimination);
		userRegister(67, elimination);
		userRegister(70, elimination);
		userRegister(73, elimination);
		
		elimination.generateChildren();
		
		Assert.assertEquals(4, elimination.getChildren().size());
		
	}
}

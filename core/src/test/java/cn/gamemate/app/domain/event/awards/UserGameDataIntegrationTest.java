package cn.gamemate.app.domain.event.awards;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class UserGameDataIntegrationTest {

	static ApplicationContext context;

	@BeforeClass
	public static void classSetup() {
		context = new ClassPathXmlApplicationContext("core.xml");
	}

	@Test
	public void testFind() {
		UserGameData data = UserGameData.findUserGameData(300, 2);
		Assert.assertNotNull(data);
		Assert.assertEquals(300, data.getUserID());
		Assert.assertEquals(2, data.getGameID());

	}
	
	@Test
	@Transactional
	public void testFindAndUpdate2(){
		//find
		UserGameData data = UserGameData.findUserGameData(300, 2);
		int p = data.power;
		
		//update
		data.power = p+1;
		
		data.total += 1;
		
		data.flush();
	}
	
	/*
	public void testFindAndUpdate() {
		
		PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
		new TransactionTemplate(transactionManager).execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				//find
				UserGameData data = UserGameData.findUserGameData(300, 2);
				int p = data.getPower();
				
				//update
				data.setPower(p+1);
			}
		});
		

	}*/

	
	
	
}

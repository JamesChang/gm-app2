package cn.gamemate.app.domain.event.awards;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.safehaus.uuid.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.gamemate.app.domain.arena.Battle;

public class UserGameAwardsUpdatorTest {
	
	@Before
	public void setUp() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
		"RDBMS_Cassandra.xml");
	}
	
	@Test
	public void test(){
		
		//init awards
		BattleAwards battleAwards = new BattleAwards();
		BattlePlayerAwards z1Awards = new BattlePlayerAwards();
		z1Awards.power = 1;
		battleAwards.put(269, z1Awards);
		
		//load battle
		Battle battle = Battle.get(new UUID("1b05ed3f-82a6-11e0-9aa3-dd7a6c13a8be"), Battle.class);
		
		//set to old value + 1
		if (battle.getUserGameAwards().containsKey(269)){
				double n = 1.0 + battle.getUserGameAwards().get(269).power;
				z1Awards.power = n;
			
		}
		UserGameData oldUserData = UserGameData.findUserGameData(269, 2);
		int oldUserPower =  oldUserData.power;
		
		
		UserGameAwardsUpdator updator = new UserGameAwardsUpdator();
		updator.update(battle, battleAwards);
		
		
		//check battle
		Battle battleAgain = Battle.get(new UUID("1b05ed3f-82a6-11e0-9aa3-dd7a6c13a8be"), Battle.class);
		Assert.assertEquals(z1Awards.power, battleAgain.getUserGameAwards().get(269).power);
		
		UserGameData userGameData = UserGameData.findUserGameData(269, 2);
		Assert.assertEquals(oldUserPower + z1Awards.power, (double)userGameData.power);
		
		
	}
	
}

package cn.gamemate.app.domain.event.dota;

import java.util.Arrays;

import me.prettyprint.hector.api.Keyspace;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.event.awards.AwardsPackage;
import cn.gamemate.app.domain.event.awards.CommonAwardsCalculator;
import cn.gamemate.app.domain.event.awards.UserGameAwardsUpdator;
import cn.gamemate.app.domain.event.dota.cass.CassandraDotaBattleDao;

public class DotaRepProcessingServiceTest {
	ApplicationContext context;
	CassandraDotaBattleDao dao;
	DefaultDotaRepProcessingService service ;
	
	final String quit_2v2 = "ef48b184-85dd-11e0-9aa3-dd7a6c13a8be";
	final int t1 = 125;
	
	
	@Before
	public void setUp(){
		context = new ClassPathXmlApplicationContext("core.xml");
		//context = new  AnnotationConfigApplicationContext(DotaRepInfoTestConfiguration.class);
		dao = new CassandraDotaBattleDao(context.getBean("gmKeyspace", Keyspace.class));
	}
	
	//@Test
	public void test(){
		service = new DefaultDotaRepProcessingService(dao);
		service.processReplay(quit_2v2,t1);
		
	}
	
	//@Test
	public void testReceiveTasks() throws Exception{
		service = new DefaultDotaRepProcessingService(dao);
		service.start();
		
		Thread.sleep(1000*3);
	}

	
	@Test
	public void testCommonAwards(){
		service = new DefaultDotaRepProcessingService(dao){
			@Override
			protected Iterable<AwardsPackage> getAwardsPackages(Battle battle) {
				AwardsPackage awardsPackage = new AwardsPackage();
				awardsPackage.setUpdator(new UserGameAwardsUpdator());
				awardsPackage.setCalculators(Arrays.asList(new CommonAwardsCalculator()));
				return Arrays.asList(awardsPackage);
			}
			
		};
		service.processReplay(quit_2v2,t1, true);
		
		
		
	}
}

package cn.gamemate.app.domain.event.dota;

import junit.framework.Assert;
import me.prettyprint.hector.api.Keyspace;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cn.gamemate.app.domain.event.dota.cass.CassandraDotaBattleDao;

public class CassandraDotaBattleDaoIntegerationTest {
	
	ApplicationContext context;
	CassandraDotaBattleDao dao;
	
	@Before
	public void setUp(){
		context = new  AnnotationConfigApplicationContext(DotaRepInfoTestConfiguration.class);
		dao = new CassandraDotaBattleDao(context.getBean("gmKeyspace", Keyspace.class));
		
	}
	
	@Test
	public void simple(){
		DotaRepInfo repInfo = dao.getReplayInfo("3f59f44e-7a10-11e0-ab86-db173d49233e", 300);
		Assert.assertEquals(300, repInfo.getSubmitter_id());
	}
	
	
}

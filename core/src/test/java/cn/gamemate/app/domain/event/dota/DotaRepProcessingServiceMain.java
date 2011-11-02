package cn.gamemate.app.domain.event.dota;

import me.prettyprint.hector.api.Keyspace;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cn.gamemate.app.domain.event.dota.cass.CassandraDotaBattleDao;

public class DotaRepProcessingServiceMain {
	static ApplicationContext context;
	static CassandraDotaBattleDao dao;
	static DefaultDotaRepProcessingService service ;
	
	
	public static void main(String[] args){
		context = new  AnnotationConfigApplicationContext(DotaRepInfoTestConfiguration.class);
		dao = new CassandraDotaBattleDao(context.getBean("gmKeyspace", Keyspace.class));
		
		service = new DefaultDotaRepProcessingService(dao);
		service.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
				
				System.out.println("shuting down...");
			};
		});
		
	}
}

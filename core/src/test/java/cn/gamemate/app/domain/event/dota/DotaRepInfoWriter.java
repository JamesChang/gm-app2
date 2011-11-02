package cn.gamemate.app.domain.event.dota;

import java.io.FileWriter;
import java.io.IOException;

import me.prettyprint.hector.api.Keyspace;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cn.gamemate.app.domain.event.dota.cass.CassandraDotaBattleDao;

public class DotaRepInfoWriter {
	
	static public void main(String [] args){
		ApplicationContext context;
		CassandraDotaBattleDao dao;
		DefaultDotaRepProcessingService service ;
		
		final String t = "e107c208-7baf-11e0-ab86-db173d49233e";
		final int u = 300;
		
		context = new  AnnotationConfigApplicationContext(cn.gamemate.app.domain.event.dota.DotaRepInfoTestConfiguration.class);
		dao = new CassandraDotaBattleDao(context.getBean("gmKeyspace", Keyspace.class));
		String data = dao.getReplayInfoAsString(t, u);
		

		try {
			FileWriter writer = new FileWriter("/home/jameszhang/w/app-parent/core/src/test/resources/dota_cdp_output/"
					+ t + "_" + u);
			writer.write(data);
			writer.close();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

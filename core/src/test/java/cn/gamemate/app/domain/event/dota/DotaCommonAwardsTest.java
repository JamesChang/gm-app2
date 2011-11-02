package cn.gamemate.app.domain.event.dota;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import cn.gamemate.app.domain.event.awards.AwardsPackage;
import cn.gamemate.app.domain.event.awards.CommonAwardsCalculator;
import cn.gamemate.app.domain.event.awards.UserGameAwardsUpdator;

public class DotaCommonAwardsTest {
	
	DotaRepInfo finished5v5_z1_win;
	
	@Before
	public void setUp() throws Exception{
		
		String prefix = "/dota_cdp_output/";
		
		/* 5v5 Finished.*/
		finished5v5_z1_win = DotaRepInfo.fromJson(
				load(prefix + "3f59f44e-7a10-11e0-ab86-db173d49233e"));
	}
	

	private String load(String filename) throws IOException{
		
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						getClass().getResourceAsStream(filename)));
		StringBuilder sb = new StringBuilder();
		String s;
		while((s = reader.readLine())!=null){
			sb.append(s).append("\n");
		}
		reader.close();
		return sb.toString();
	}
/*	AwardsPackage awardsPackage = new AwardsPackage();
	awardsPackage.setUpdator(new UserGameAwardsUpdator());
	awardsPackage.setCalculators(Arrays.asList(new CommonAwardsCalculator()));
*/	
	
	
	
	
	
}

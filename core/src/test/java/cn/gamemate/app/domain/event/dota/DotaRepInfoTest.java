package cn.gamemate.app.domain.event.dota;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.gamemate.app.domain.event.dota.DotaRepInfo.DotaPlayer;


public class DotaRepInfoTest {
	
	DotaRepInfo finished5v5_z1_win;
	DotaRepInfo quit2v2_s1_win;
	DotaRepInfo quit2v2_s1_loss;
	DotaRepInfo unknown3v3_t2;
	
	@Before
	public void setUp() throws Exception{
		
		String prefix = "/dota_cdp_output/";
		
		/* 5v5 Finished.*/
		finished5v5_z1_win = DotaRepInfo.fromJson(
				load(prefix + "3f59f44e-7a10-11e0-ab86-db173d49233e"));
		
		/* 2v2 Quit 
		 * y1, z1 先退出。s1, t1胜利.
		 * */
		quit2v2_s1_win = DotaRepInfo.fromJson(
				load(prefix + "e107c208-7baf-11e0-ab86-db173d49233e_279"));
		quit2v2_s1_loss = DotaRepInfo.fromJson(
				load(prefix + "e107c208-7baf-11e0-ab86-db173d49233e_300"));
		
		/*pub, 3v3. 
		 * 主机huangzhe先退出，主机自动转移。
		 * 然后大家接着就退出了。
		 * 虽然大家都上传了replay，但是这场没有胜负。
		 * 因为在“游戏暂停”的过程中，大家就都退出了。
		 * 
		 * (u'huangzhe', 1, 14, 302258)
		 * (u't2', u'Disconnect', 12, 346288)
		 * (u'y3', u'Disconnect', 12, 346288)
		 * (u'c6', u'Disconnect', 12, 346288)
		 * (u'y4', u'Disconnect', 12, 346288)
		 * (u's1', u'Left', 1, 342341)
		 * 
		 */
		unknown3v3_t2 = DotaRepInfo.fromJson(
				load(prefix + "346fba4b-8093-11e0-8db7-bf2656859e9f_126"));
		
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
	
	@Test
	public void testBasicInfo(){
		
		
		Assert.assertEquals(269, finished5v5_z1_win.getSubmitter_id());
		Assert.assertEquals( "z1", finished5v5_z1_win.getSubmitter_name());
		
		Assert.assertEquals(279, quit2v2_s1_win.getSubmitter_id());
		Assert.assertEquals( "s1", quit2v2_s1_win.getSubmitter_name());
		
		Assert.assertEquals(300, quit2v2_s1_loss.getSubmitter_id());
		Assert.assertEquals( "y1", quit2v2_s1_loss.getSubmitter_name());
		
		
	}
	
	@Test
	public void testGetEffectivePlayTime(){
		Assert.assertEquals(1201, finished5v5_z1_win.getEffectivePlayTime()/1000);
		Assert.assertEquals(248, quit2v2_s1_win.getEffectivePlayTime()/1000);
		Assert.assertEquals(243, quit2v2_s1_loss.getEffectivePlayTime()/1000);
		Assert.assertEquals(346, unknown3v3_t2.getFullPlayTime()/1000);
	}
	
	@Test
	public void testGetFullPlayTime(){
		Assert.assertEquals(1201, finished5v5_z1_win.getFullPlayTime()/1000);
		Assert.assertEquals(256, quit2v2_s1_win.getFullPlayTime()/1000);
		Assert.assertEquals(243, quit2v2_s1_loss.getFullPlayTime()/1000);
		Assert.assertEquals(346, unknown3v3_t2.getFullPlayTime()/1000);
	}
	
	@Test
	public void testIsFinished(){
		Assert.assertEquals(true, finished5v5_z1_win.isFinished());
		Assert.assertEquals(false, quit2v2_s1_win.isFinished());
		Assert.assertEquals(false, quit2v2_s1_loss.isFinished());
		Assert.assertEquals(false, unknown3v3_t2.isFinished());
	}
	
	@Test
	public void testIsQuit(){
		Assert.assertEquals(false, finished5v5_z1_win.isQuit());
		Assert.assertEquals(true, quit2v2_s1_win.isQuit());
		Assert.assertEquals(false, quit2v2_s1_loss.isQuit());
		Assert.assertEquals(false, unknown3v3_t2.isQuit());
	}
	
	
	@Test
	public void testGetWinnerForceId(){
		Assert.assertEquals((Integer)1, finished5v5_z1_win.getWinnerForceId());
		Assert.assertEquals((Integer)0, quit2v2_s1_win.getWinnerForceId());
		Assert.assertEquals(null, quit2v2_s1_loss.getWinnerForceId());
	}
	
	@Test
	public void testGetOutcome(){

		Assert.assertEquals((Float)2.0f, finished5v5_z1_win.getOutcome().get(0));
		Assert.assertEquals((Float)1.0f, finished5v5_z1_win.getOutcome().get(1));
		

		Assert.assertEquals((Float)1.0f, quit2v2_s1_win.getOutcome().get(0));
		Assert.assertEquals((Float)2.0f, quit2v2_s1_win.getOutcome().get(1));
		
		Assert.assertEquals(null, quit2v2_s1_loss.getOutcome());
		
		Assert.assertEquals(null, unknown3v3_t2.getOutcome());
	}
	
	@Test
	public void testGetPlayerCount(){
		Assert.assertEquals(10, finished5v5_z1_win.getPlayerCount());
		Assert.assertEquals(4, quit2v2_s1_win.getPlayerCount());
		Assert.assertEquals(4, quit2v2_s1_loss.getPlayerCount());
		Assert.assertEquals(6, unknown3v3_t2.getPlayerCount());
	}
	
	@Test
	public void testGetQuitPlayers() {
		List<DotaPlayer> quitPlayers = finished5v5_z1_win.getQuitPlayers(60*3*1000);
		Assert.assertEquals(0, quitPlayers.size());
		
		//无人3分钟内早退
		Assert.assertEquals(0, quit2v2_s1_win.getQuitPlayers(60*3*1000).size());
		//2人250秒退出		
		Assert.assertEquals(2, quit2v2_s1_win.getQuitPlayers(250*1000).size());
		//还是2人，最后胜利的2人不算退出。		
		Assert.assertEquals(2, quit2v2_s1_win.getQuitPlayers(60*5*1000).size());
		
		// zero, actually.
		//Assert.assertEquals(2, quit2v2_s1_loss.getQuitPlayers(60*5*1000).size());
		
		
		Assert.assertEquals(2, unknown3v3_t2.getQuitPlayers().size());
		
	}
	
	
	
	
	
	
}

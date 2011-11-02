package cn.gamemate.app.domain.event.rts;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/rdbms.xml"})
public class RtsTeamTest {

    @Test
    public void test() {
    	
    	RtsTeam team = RtsTeam.findRtsTeam("77cff450-be3f-4848-ba7c-b2b030ccb49b");
    	Assert.assertNotNull(team);
    	Assert.assertEquals(Integer.valueOf(2), team.getLgid());
    	Assert.assertEquals("77cff450-be3f-4848-ba7c-b2b030ccb49b", team.getId());
    	Assert.assertNotNull(team.getLeader());
    	Assert.assertEquals(64,team.getLeader().getId() );
    	
    }
}

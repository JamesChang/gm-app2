package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import proto.response.ResEvent;

import com.google.common.collect.Lists;

import cn.gamemate.app.domain.event.rts.PlayerRegisterationSupport;
import cn.gamemate.app.domain.event.rts.RtsEventForce;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.DomainModelView;
import cn.gamemate.app.domain.DomainModelViewSupport;

public class RegistrationTests {
	
	private void userRegister(String userName, PlayerRegisterationSupport event){
		User user = User.findArenaUserByName(userName);
		Assert.assertNotNull(user);
		event.playerRegister(user, null);
	}
	
	private List<RtsEventForce> getCheckedForces(PlayerRegisterationSupport event){
		ArrayList<RtsEventForce> rst = Lists.newArrayList();
	
		for(RtsEventForce force:event.getRegisteredForces()){
			if (force.isChecked()){
				rst.add(force);
			}
		}
		return rst;
	}
	
	public void runAllTests(PlayerRegisterationSupport event){
		testExtraCheck(event);

		if (event instanceof DomainModelViewSupport){
			DomainModelViewSupport e = (DomainModelViewSupport) event;
			testRegistrationManagementView(e);
		}
	}
	
	void testExtraCheck(PlayerRegisterationSupport event){

		
		Date startTime = new org.joda.time.DateTime().minusHours(1).toDate();
		Date endTime = new org.joda.time.DateTime().plusHours(1).toDate();
		event.setExpectedExtraCheckStartTime(startTime);
		event.setExpectedExtraCheckStartTime(endTime);
		User user1 = User.findArenaUserByName("user1");
		User user2 = User.findArenaUserByName("user2");
		userRegister("user1",event);
		userRegister("user2",event);
		Assert.assertEquals(2, event.getRegisteredForceCount());
		
		event.setAutoExtraCheck(true);
		Assert.assertEquals(true, event.isAutoExtraCheck());
		Assert.assertEquals(0,getCheckedForces(event).size());
		
		event.playerCheckIn(user1,null);
		Assert.assertEquals(1,getCheckedForces(event).size());
		
		if (event instanceof DomainModelViewSupport){
			DomainModelViewSupport e = (DomainModelViewSupport) event;
			DomainModelView view1 = e.newView(user1);
			DomainModelView view2 = e.newView(user2);
			Assert.assertEquals(false, ((ResEvent.EventGet.Builder)(view1.toProtobuf())).getCheckInRequired());
			Assert.assertEquals(true, ((ResEvent.EventGet.Builder)(view2.toProtobuf())).getCheckInRequired());
		}
		
		event.playerCheckIn(user2,null);
		Assert.assertEquals(2,getCheckedForces(event).size());
		
		
	}
	
	void testRegistrationManagementView(DomainModelViewSupport event){
		//user1 must be administrator
		User user1 = User.findArenaUserByName("user1");
		DomainModelView view = event.newView(user1, "registration.manage");
		System.out.println(view.toProtobuf().build());
		
	}
	
}

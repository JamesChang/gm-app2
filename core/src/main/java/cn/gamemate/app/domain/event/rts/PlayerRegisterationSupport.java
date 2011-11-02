package cn.gamemate.app.domain.event.rts;

import java.util.List;

import cn.gamemate.app.domain.user.User;

public interface PlayerRegisterationSupport {
	void playerRegister(User player, RtsTeam team);
	void playerUnregister(User player, RtsTeam team);
	void administratorUnregister(User operator, User player, RtsTeam team);
	void setRequiredPlayerInfo(List<String> requiredFields);
	List<String>  getRequiredPlayerInfo();
	int getRegisteredForceCount();
	List<RtsEventForce> getRegisteredForces();
	void playerCheckIn(User player, RtsTeam team);

	//- properties ------------------------------------------
	java.util.Date getExpectedRegistrationStartTime();
	void setExpectedRegistrationStartTime(java.util.Date t);
	
	java.util.Date getExpectedRegistrationEndTime();
	void setExpectedRegistrationEndTime(java.util.Date t);
	
	void setRegistrationLimit(Integer registrationLimit);
	Integer getRegistrationLimit();
	
	void setAllowPlayerRegister(boolean allowPlayerRegister);
	boolean isAllowPlayerRegister();
	
	void setAllowPlayerRegistrationAfterPrepared(
			boolean allowPlayerRegistrationAfterPrepared);
	boolean isAllowPlayerRegistrationAfterPrepared();
	
	//注册+签到 -------------------------------------------------
	java.util.Date getExpectedExtraCheckStartTime();
	void setExpectedExtraCheckStartTime(java.util.Date t);
	java.util.Date getExpectedExtraCheckEndTime();
	void setExpectedExtraCheckEndTime(java.util.Date t);
	void setAutoExtraCheck(boolean enabled);
	boolean isAutoExtraCheck();
}

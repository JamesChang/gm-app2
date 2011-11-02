package cn.gamemate.app.domain.event;

import cn.gamemate.app.domain.user.User;

public interface RegistrationManagementSupport {
	
	void userStartRegistration(User user);
	void userStopRegistration(User user);

}

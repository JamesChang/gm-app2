package cn.gamemate.app.domain.event;

import cn.gamemate.app.domain.Forbidden;
import cn.gamemate.app.domain.user.User;

public interface PermissionSupport {
	public void assertPermission(User user, String name) throws Forbidden;
	
	
	public boolean checkPermission(User user, String name);
	
	public Iterable<User> findUsersByRole(String name);
}

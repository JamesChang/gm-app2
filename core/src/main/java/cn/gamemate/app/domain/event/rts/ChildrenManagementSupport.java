package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.user.User;

public interface ChildrenManagementSupport {
	public void generateChildren();
	public void userGenerateChildren(User operator);
}

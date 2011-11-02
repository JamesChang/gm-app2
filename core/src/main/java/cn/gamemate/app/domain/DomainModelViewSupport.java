package cn.gamemate.app.domain;

import cn.gamemate.app.domain.user.User;

public interface DomainModelViewSupport {
	
	public DomainModelView newView(User operator, String viewName);
	public DomainModelView newView(User operator);

}

package cn.gamemate.app.domain.event;

import java.util.List;

public interface ChildrenSupport {
	List<? extends Event> getChildren();
}

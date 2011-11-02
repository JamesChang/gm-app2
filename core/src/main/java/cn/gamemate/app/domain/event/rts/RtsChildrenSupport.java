package cn.gamemate.app.domain.event.rts;

import java.util.List;

import cn.gamemate.app.domain.event.ChildrenSupport;

public interface RtsChildrenSupport extends ChildrenSupport{
	List<? extends RtsElimination> getChildren();
}

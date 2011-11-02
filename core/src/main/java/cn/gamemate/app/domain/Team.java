package cn.gamemate.app.domain;

import java.util.Collection;

public interface Team<U> {
	Collection<U> getPlayers();
	String getId();
	String getName();
}

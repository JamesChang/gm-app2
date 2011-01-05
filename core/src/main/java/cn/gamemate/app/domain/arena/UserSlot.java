package cn.gamemate.app.domain.arena;

import java.util.Map;

import cn.gamemate.app.domain.user.User;

public interface UserSlot {
	Map<String, String> getExtra();
	User getUser();
}

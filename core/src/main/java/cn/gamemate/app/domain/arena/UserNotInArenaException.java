package cn.gamemate.app.domain.arena;

import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.user.User;

public class UserNotInArenaException extends DomainModelRuntimeException{
	public UserNotInArenaException() {
		super("specified user is not in this specified arena");
	}
	public UserNotInArenaException(User user, Arena a){
		super(new StringBuilder().append("user ").append(user.getName()).append(":").append(user.getId())
				.append(" is not in arena ").append(a.getUuid()).toString());
	}
	
	@Override
	public long getErrorCode() {
		return 419006;
	}
}

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.DomainModelRuntimeException;

public class UserDetailedInfoRequired extends DomainModelRuntimeException{
	
	public UserDetailedInfoRequired() {
		super("User Detailed Info Required");
	}
	
	public UserDetailedInfoRequired(String msg){
		super(msg);
	}
	
	public long getErrorCode(){
		return 419020;
	}
}

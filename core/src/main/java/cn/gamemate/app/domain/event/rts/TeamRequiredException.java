package cn.gamemate.app.domain.event.rts;



import cn.gamemate.app.domain.DomainModelRuntimeException;

public class TeamRequiredException extends DomainModelRuntimeException{
	public TeamRequiredException() {
		super("Team Required");
	}
	public TeamRequiredException(String msg){
		super(msg);
	}
	
	public long getErrorCode(){
		return 419017;
	}
}


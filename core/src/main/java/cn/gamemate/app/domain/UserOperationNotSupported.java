package cn.gamemate.app.domain;

public class UserOperationNotSupported extends DomainModelRuntimeException{

	public UserOperationNotSupported() {
		this("UserOperationNotSupported");
	}
	
	public UserOperationNotSupported(String msg) {
		super(msg);
	}
	
	public UserOperationNotSupported(String msg, Throwable e) {
		super(msg, e);
	}
	
	public long getErrorCode(){
		return 433000;
	}
}

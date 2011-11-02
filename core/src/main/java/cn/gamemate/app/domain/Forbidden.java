package cn.gamemate.app.domain;

public class Forbidden extends DomainModelRuntimeException{
	
	public Forbidden() {
		super("没有权限");
	}
	public Forbidden(String msg) {
		super(msg);
	}
	public Forbidden(String msg, Throwable e) {
		super(msg, e);
	}
	
	public long getErrorCode(){
		return 403000;
	}
}

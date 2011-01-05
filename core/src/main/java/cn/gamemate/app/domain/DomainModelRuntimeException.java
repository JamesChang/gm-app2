package cn.gamemate.app.domain;

import org.springframework.core.NestedRuntimeException;

public class DomainModelRuntimeException extends NestedRuntimeException{
	
	
	public DomainModelRuntimeException(String msg) {
		super(msg);
	}
	
	
	public DomainModelRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public long getErrorCode(){
		return 419000;
	}
	
	
}

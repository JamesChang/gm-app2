package cn.gamemate.app.domain.arena;

import cn.gamemate.app.domain.DomainModelRuntimeException;

public class ArenaFullException extends DomainModelRuntimeException{
	public ArenaFullException() {
		super("Arena Full");
	}
	public ArenaFullException(String msg){
		super(msg);
	}
	
	public long getErrorCode(){
		return 419004;
	}
}

package cn.gamemate.app.domain;

public class NotFullySupportedException extends DomainModelRuntimeException{
	
	

	public NotFullySupportedException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long getErrorCode(){
		return 549000;
	}
}

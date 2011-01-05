package cn.gamemate.app.clientmsg;

public class MessageServiceException extends RuntimeException {

	public MessageServiceException(String msg) {
		super(msg);
	}

	public MessageServiceException() {
		super();
	}

	public MessageServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageServiceException(Throwable cause) {
		super(cause);
	}
	

	
}

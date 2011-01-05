package cn.gamemate.app.clientmsg;



public interface MessageService {
	

	 public void send(ClientMessage message) throws MessageServiceException;
	 public void asyncSend(ClientMessage message);
	 
	 public void checkConnection() throws MessageServiceException;
}

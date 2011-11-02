package cn.gamemate.app.clientmsg;

public class HelloMessage extends ClientMessage{
	
	@Override
	public int getCode() {
		return 0x2000;
	}

}

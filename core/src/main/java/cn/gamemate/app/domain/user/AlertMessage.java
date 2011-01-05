package cn.gamemate.app.domain.user;

import java.util.List;

import proto.msg.MsgAlert;
import cn.gamemate.app.clientmsg.ClientMessage;

public class AlertMessage extends ClientMessage{
	
	public AlertMessage(User target, String text) {
		this(target, text, false);
	}
	public AlertMessage(User target, String text, boolean modal) {
		receivers.add(target.getId());
		rootBuilder.setAlert(MsgAlert.AlertMessage.newBuilder()
				.setText(text).setModal(modal).setUri(""));
	}
	public AlertMessage(List<Integer> receivers, String text, boolean modal){
		this.receivers.addAll(receivers);
		rootBuilder.setAlert(MsgAlert.AlertMessage.newBuilder()
				.setText(text).setModal(modal).setUri(""));
	}

	@Override
	public int getCode() {
		return 0x2501;
	}

}

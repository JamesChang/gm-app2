package cn.gamemate.app.domain.party;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import proto.response.ResParty;

import cn.gamemate.app.domain.arena.UserSlot;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.User.UserStatus;

public class PartyMember implements Serializable, UserSlot {

	private static final long serialVersionUID = 1L;

	private User user;
	private Map<String, String> extra;
	private boolean isOut=true;

	public PartyMember(User user) {
		// TODO: optimize this, no need to create
		this.user = user;
		this.extra = new TreeMap<String, String>();
	}

	@Override
	public Map<String, String> getExtra() {
		return extra;
	}

	@Override
	public User getUser() {
		return user;
	}

	public void clear() {
		this.user = null;
		setExtraToDefault();
	}

	private void setExtraToDefault() {
		this.extra.clear();
	}
	
	public boolean isOut(){
		return isOut;
	}
	public boolean isDrop(){
		return user.getStatus() == UserStatus.DROP;
	}
	public void setOut(boolean b){
		isOut = b;
	}
	
	
	public ResParty.PartyMember.Builder toProtobuf() {
		ResParty.PartyMember.Builder builder= ResParty.PartyMember.newBuilder();
		builder.setUser(user.toProtobuf()).setIsOut(isOut).setIsUpdating(false);
		return builder;
	}
	
	public void setAttribute(String key, String value){
		extra.put(key, value);
	}

}

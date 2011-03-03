package cn.gamemate.app.domain.arena;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.gamemate.app.domain.attr.Field;
import cn.gamemate.app.domain.user.User;

public class ArenaSlot implements Serializable, UserSlot{

	private static final long serialVersionUID = 1L;
	private User user;
	private ArenaForce force;
	private boolean enabled;
	private boolean ready;
	private boolean readyDefault;
	private boolean gaming;
	private int position;
	private Map<String, String> extra;
	private Arena arena;
	

	public ArenaSlot(Arena arena, User user, ArenaForce force, boolean ready, int position,
			Map<String, String> extra) {
		super();
		this.arena = arena;
		this.user = user;
		this.force = force;
		this.readyDefault = ready;
		this.ready = false;
		this.gaming = false;
		if (extra == null){
			this.extra = new HashMap<String, String>(10);
		}
		else{
			this.extra = new HashMap<String, String>(extra) ;
		}
		
		this.position = position;
		this.enabled = true;
	}

	public int getPosition() {
		return position;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		setExtraToDefault();
		this.ready = readyDefault;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void enable() {
		this.enabled = true;
	}
	public void disable() {
		this.enabled = false;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
	public boolean toggleReady(){
		return this.ready = !this.ready;
	}
	

	public boolean isGaming() {
		return gaming;
	}

	public void setGaming(boolean gaming) {
		this.gaming = gaming;
	}

	public ArenaForce getForce() {
		return force;
	}

	public Map<String, String> getExtra() {
		return extra;
	}
	public void clear(){
		this.setUser(null);
		setExtraToDefault();
		this.ready = false;
		this.gaming = false;
	}
	
	private void setExtraToDefault(){
		this.extra.clear();
		for (Map.Entry<String, Field> item: arena.userAttrDefs.entrySet()){
			this.extra.put(item.getKey(), item.getValue().getInitial());
		}
	}
	
}

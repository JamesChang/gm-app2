package cn.gamemate.app.domain.arena;

import proto.res.ResArena;
import proto.res.ResArena.BattleForce.Builder;

public class ArenaForce {
	protected String label;
	protected int id;
	protected Arena arena;
	
	public String getLabel() {
		return label;
	}
	public int getId() {
		return id;
	}
	public Arena getArena() {
		return arena;
	}
	
	public ResArena.BattleForce.Builder toProtobuf(){
		ResArena.BattleForce.Builder builder = ResArena.BattleForce.newBuilder();
		copyTo(builder);
		return builder;
	}
	private void copyTo(Builder builder) {
		builder.setLabel(label).setId(id);
		
	}
	
}

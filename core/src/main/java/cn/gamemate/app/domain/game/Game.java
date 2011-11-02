package cn.gamemate.app.domain.game;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Transient;

import proto.response.ResGame;
import proto.response.ResGame.LogicalGame;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.attr.Field;

import com.google.common.base.Objects;
import com.google.protobuf.GeneratedMessage.Builder;




// 值对象

public class Game implements DomainModel{
	
	//these games are instanced at DefaultGameDefinition
	static Game war3;
	static Game dota;
	static Game sc;
	static Game lol;
	static Game cs;
	
	private String name;
	private int id;
	@Transient
	private Map<String, Field> userAttrs = new LinkedHashMap<String, Field>();
	private PhysicalGame physicalGame;

	/**
	 * @return the physicalGame
	 */
	public PhysicalGame getPhysicalGame() {
		return physicalGame;
	}

	/**
	 * @param physicalGame the physicalGame to set
	 */
	public void setPhysicalGame(PhysicalGame physicalGame) {
		this.physicalGame = physicalGame;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static Game findById(int id){
		
		switch(id){
		case 1:
			return war3;
		case 2:
			return dota;
		case 3:
			return sc;
		case 5:
			return lol;
		case 6:
			return cs;
		default:
		}
		//TODO 
		throw new RuntimeException("can not reach here");
		
	}

	public Map<String, Field> getUserAttrs() {
		return userAttrs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void addUserAttribute(String key, Field attribute){
		this.userAttrs.put(key, attribute);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("id", id)
			.add("name",name)
			.toString();
	}

	@Override
	public ResGame.LogicalGame.Builder toProtobuf() {
		proto.response.ResGame.LogicalGame.Builder lgBuilder =
			LogicalGame.newBuilder().setId(getId()).setName(getName());
		return lgBuilder;
	}
	
}

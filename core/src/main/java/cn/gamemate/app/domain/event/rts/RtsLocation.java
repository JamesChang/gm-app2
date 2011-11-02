package cn.gamemate.app.domain.event.rts;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import cn.gamemate.app.domain.event.EventForce;
import cn.gamemate.app.domain.user.User;


/**
 * @author jameszhang
 *
 */

@Embeddable
public class RtsLocation{
	
	public static String HF = "hf";
	
	public static String VS = "vs";
	
	public static String SJTU_BN = "sjtubn";
	
	public static String NULL = "--";
	
	public static RtsLocation newHf(){
		RtsLocation result = new RtsLocation();
		result.setDiscriminatorValue(HF);
		return result;
	}
	
	public static RtsLocation newVs(){
		RtsLocation result = new RtsLocation();
		result.setDiscriminatorValue(VS);
		return result;
	}
	
	public static RtsLocation newNull(){
		RtsLocation result = new RtsLocation();
		result.setDiscriminatorValue(NULL);
		return result;
	}
	

	public static RtsLocation newSjtuBn(){
		RtsLocation result = new RtsLocation();
		result.setDiscriminatorValue(SJTU_BN);
		return result;
	}
	
	public RtsLocation() {
	}
	
	protected String discriminatorValue;
	
	public String getDiscriminatorValue() {
		return discriminatorValue;
	}

	public RtsLocation setDiscriminatorValue(String discriminatorValue) {
		this.discriminatorValue = discriminatorValue;
		return this;
	}

	@Transient
	public String getName(){
		if (discriminatorValue.equals(HF)){
			return "浩方";
		}else if (discriminatorValue.equals(VS)){
			return "VS";
		}else if (discriminatorValue.equals(SJTU_BN)){
			return "上海交大BN";
		}else if (discriminatorValue.equals(NULL)){
			return "--";
		}
		return "--";
	}
	
	@Transient
	public String getUserName(User user){
		if (discriminatorValue.equals(HF)){
			return user.getHfName();
		}else if (discriminatorValue.equals(VS)){
			return user.getVsName();
		}else if (discriminatorValue.equals(SJTU_BN)){
			return user.getSjtubn();
		}
		return "";
	}
	
	public boolean checkQualification(EventForce force){
		if (discriminatorValue.equals(NULL)){return true;}
		for(User player:force.getPlayers()){
			String playerID = getUserName(player);
			if (playerID == null || player.equals("")){
				return false;
			}
		}
		return true;
	}
	
	
}

// JPA不知道如何支持下面的东西？

/*
@Embeddable
abstract public class RtsLocation {
	
	protected String discriminatorValue;
	
	abstract public String getName();
	
	abstract public String getUserName(User user);
	
	public boolean checkQualification(EventForce force){
		for(User player:force.getPlayers()){
			String playerID = getUserName(player);
			if (playerID == null || player.equals("")){
				return false;
			}
		}
		return true;
	}
	
	
	
}

class RtsHfLocation extends RtsLocation{
	
	public RtsHfLocation() {
		this.discriminatorValue = "hf";
	}

	@Override
	public String getName() {
		return "浩方";
	}

	@Override
	public String getUserName(User user) {
		return user.getHfName();
	}
	
}

class RtsVsLocation extends RtsLocation{
	
	
	public RtsVsLocation() {
		this.discriminatorValue = "rts";
	}
	
	@Override
	public String getName() {
		return "VS";
	}

	@Override
	public String getUserName(User user) {
		return user.getVsName();
	}
	
	
	
}
*/


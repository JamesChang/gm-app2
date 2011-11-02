package cn.gamemate.app.domain.event.rts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import cn.gamemate.app.domain.user.User;

@RooJavaBean
public class PlayerInfoChecker {
	
	Iterable<String> requiredFields;
	
	public static Map<String, String> humanFriendlyFieldNames;
	
	//TODO: move this to spring context
	static{
		humanFriendlyFieldNames = new HashMap<String, String>();
		humanFriendlyFieldNames.put("truename","真实姓名");
		humanFriendlyFieldNames.put("qq","QQ号码");
		humanFriendlyFieldNames.put("mobile","手机号码");
		humanFriendlyFieldNames.put("chineseId","身份证号码");
		humanFriendlyFieldNames.put("address","地址");
		humanFriendlyFieldNames.put("vsName","VS名字");
		humanFriendlyFieldNames.put("sjtubn","交大战网帐号");
		humanFriendlyFieldNames.put("hfName","浩方名字");
	}
	
	public PlayerInfoChecker setRequiredFields(Iterable<String> requiredFields){
		this.requiredFields = requiredFields;
		return this;
	}
	
	public void check(User user) throws UserDetailedInfoRequired{
		for(String fieldName:requiredFields){
			String value = getValueFromUser(user, fieldName);
			if (value==null || "".equals(value)){
				UserDetailedInfoRequired required = new UserDetailedInfoRequired("User Info Required");
				required.setData("[\"" + Joiner.on("\",\"").join(requiredFields) + "\"]");
				throw required;
			}
		}
	}
	
	public static String getHumanFriendlyFieldName(String fieldName){
		return humanFriendlyFieldNames.get(fieldName);
	}
	
	public static String getValueFromUser(User user, String fieldName) {
		try {
			Method method = user.getClass().getMethod("get"+fieldName.substring(0,1).toUpperCase() + fieldName.substring(1));
			Object value = method.invoke(user);
			return (String) value;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}

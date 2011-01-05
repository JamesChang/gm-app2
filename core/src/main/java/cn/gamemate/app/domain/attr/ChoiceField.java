package cn.gamemate.app.domain.attr;

import java.util.Map;

import org.apache.commons.collections.MapIterator;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class ChoiceField extends Field{
	
	private Map<String, String> valueMap;
	private Map<String, String> nameMap;
	
	public ChoiceField(Map<String, String>choices, String label, boolean required, String initial,
			String help_text) {
		super(label, required, initial, help_text);
		Builder<String, String> nameBuilder = ImmutableMap.builder();
		Builder<String, String> valueBuilder = ImmutableMap.builder();
		String firstValue=null;
		for (Map.Entry<String, String> entry: choices.entrySet()){
			if (firstValue==null) firstValue=entry.getValue();
			nameBuilder.put(entry.getKey(), entry.getValue());
			valueBuilder.put(entry.getValue(), entry.getKey());
			
		}
		if (initial==null){
			setInitial(firstValue);			
		}
		nameMap = nameBuilder.build();
		valueMap = valueBuilder.build();
		
	}
	@Override
	public boolean validate(String value){
		//TODO: super validate
		if (!nameMap.containsKey(value)){
			return false;
		}
		return true;
	}
	

	public String name2value(String name){
		return nameMap.get(name);
	}
	public String value2name(String value){
		return valueMap.get(value);
	}
	
	
	
}

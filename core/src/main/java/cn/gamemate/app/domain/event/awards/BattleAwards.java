package cn.gamemate.app.domain.event.awards;

import java.io.IOException;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public class BattleAwards extends TreeMap<Integer, BattlePlayerAwards>{

	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static BattleAwards fromJson(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, new TypeReference<BattleAwards>(){});
	}
	
	public BattlePlayerAwards getOrCreate(Integer key) {
		
		 BattlePlayerAwards playerAwards = get(key);
		 if (playerAwards == null){
			 playerAwards = new BattlePlayerAwards();
			 put(key, playerAwards);
		 }
		 return playerAwards;
		 
	}
	
	public void minus(BattleAwards other){
		 for (Entry<Integer, BattlePlayerAwards> entry: entrySet()){
			 BattlePlayerAwards otherValue= other.get(entry.getKey());
			 if (otherValue!=null){
				 entry.getValue().minus(otherValue);
			 }
		 }
	}
	
}

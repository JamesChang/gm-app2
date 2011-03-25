package cn.gamemate.app.domain.arena;

import java.io.IOException;
import java.util.TreeMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class BattleAwards extends TreeMap<Integer, BattlePlayerAwards>{

	public String toJson() throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}

	public static BattleAwards fromJson(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, new TypeReference<BattleAwards>(){});
	}
}

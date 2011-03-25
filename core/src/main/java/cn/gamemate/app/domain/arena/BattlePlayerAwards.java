package cn.gamemate.app.domain.arena;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
public class BattlePlayerAwards {
	private Integer gold = 0;
	private Integer activity = 0;
	private double power = 0.0;

	public String toJson() throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}

	public static BattlePlayerAwards fromJson(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, BattlePlayerAwards.class);
	}
}

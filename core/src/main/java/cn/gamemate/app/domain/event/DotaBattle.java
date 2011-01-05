package cn.gamemate.app.domain.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.user.User;



@Configurable
public class DotaBattle extends Battle {
	private final ObjectMapper jsonMapper = new ObjectMapper();
	/*
	@NotNull
	@Value("${dota.replay.parser}")
	private String PARSER_SCRIPT;
	
	@NotNull
	@Value("${dota.replay.parser.dir}")
	private String PARSER_SCRIPT_DIR;
	
	
	
	private void parseReplay(File repFile){
		try{
			//ProcessBuilder pb = new ProcessBuilder("php", PARSER_SCRIPT, 
			//		repFile.getAbsolutePath());
			ProcessBuilder pb = new ProcessBuilder("php", PARSER_SCRIPT, 
					repFile.getAbsolutePath());
			pb.directory(new File(PARSER_SCRIPT_DIR));
			final Process process = pb.start();
			//BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			ParsedDotaReplay parsedRep = jsonMapper.readValue(process.getInputStream(), ParsedDotaReplay.class);
			System.out.println(parsedRep);
			
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
		
	}
	*/
	private ParsedDotaReplay getParsed(File parsedFile){
		try{
			return jsonMapper.readValue(parsedFile, ParsedDotaReplay.class);
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
		
	}
	
	///////////////////////////////////////////////
	// Service Layer
	///////////////////////////////////////////////
	
	synchronized public void userSubmitReplayFile(String uri, User user){
		File repFile = new File(uri);
		if (!repFile.exists()){
			throw new RuntimeException("replay file '"+ uri + "' does not exist.");
		}
		//parseReplay(repFile);
	}
	
	public ParsedDotaReplay loadJson(String uri){
		File repFile = new File(uri);
		if (!repFile.exists()){
			throw new RuntimeException("replay file '"+ uri + "' does not exist.");
		}
		//parseReplay(repFile);
		//jsonMapper.
		ParsedDotaReplay parsedRep = getParsed(repFile);
		return parsedRep;
	}

}

package cn.gamemate.app.domain.event.awards;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.base.Objects;


@JsonIgnoreProperties(ignoreUnknown=true)
public class BattlePlayerAwards {
/*
	public double getTrueskillMean() {
		return trueskill_mean;
	}

	public void setTrueskillMean(double mean) {
		this.trueskill_mean = mean;
	}*/
/*
	public double getTrueskillSd() {
		return trueskill_sd;
	}

	public void setTrueskillSd(double sd) {
		this.trueskill_sd = sd;
	}*/

	public Integer gold = 0;
	public Integer activity = 0;
	public double power = 0.0;
	public double power_final = 0.0;
	public double trueskill_mean = 0.0;
	public double trueskill_sd = 0.0;
	public double trueskill_mean_final = 0.0;
	public double trueskill_sd_final = 0.0;
	public int total = 0;
	public int draw = 0;
	public int win = 0;
	
	//RTS
	public int first = 0;
	public int second = 0;
	public int third = 0;
	public int rtsScore = 0;

	public String toJson() throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}

	public static BattlePlayerAwards fromJson(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, BattlePlayerAwards.class);
	}

	public void minus(BattlePlayerAwards otherValue) {
		this.gold -= otherValue.gold;
		this.activity -= otherValue.activity;
		this.power -= otherValue.power;
		this.power_final -= otherValue.power_final;
		this.trueskill_mean -= otherValue.trueskill_mean;
		this.trueskill_sd -= otherValue.trueskill_sd;
		this.trueskill_mean_final -= otherValue.trueskill_mean_final;
		this.trueskill_sd_final -= otherValue.trueskill_sd_final;
		this.total -= otherValue.total; 
		this.win -= otherValue.win;
		this.draw -= otherValue.draw;
		this.first -=otherValue.first;
		this.second -=otherValue.second;
		this.third -=otherValue.third;
		this.rtsScore -=otherValue.rtsScore;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("total", this.total)
			.add("win", win)
			.add("rtsScore", rtsScore)
			.toString();
			
	}

}

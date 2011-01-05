package cn.gamemate.app.domain.event;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.springframework.roo.addon.javabean.RooJavaBean;



@JsonIgnoreProperties(ignoreUnknown=true)

public class ParsedDotaReplay{
	public Map<String, String> getWc3idToNames() {
		return wc3idToNames;
	}



	public void setWc3idToNames(Map<String, String> wc3idToNames) {
		this.wc3idToNames = wc3idToNames;
	}



	public List<Chat> getChat() {
		return chat;
	}



	public void setChat(List<Chat> chat) {
		this.chat = chat;
	}



	Map<String, String> wc3idToNames;
	List<Chat> chat;
	
	
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Chat{
		private String text;
		private Integer player_id;
		private String player_name;
		private Integer time;
		private String mode;
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public Integer getPlayer_id() {
			return player_id;
		}
		public void setPlayer_id(Integer player_id) {
			this.player_id = player_id;
		}
		public String getPlayer_name() {
			return player_name;
		}
		public void setPlayer_name(String player_name) {
			this.player_name = player_name;
		}
		public Integer getTime() {
			return time;
		}
		public void setTime(Integer time) {
			this.time = time;
		}
		public String getMode() {
			return mode;
		}
		public void setMode(String mode) {
			this.mode = mode;
		}
	
	}
}

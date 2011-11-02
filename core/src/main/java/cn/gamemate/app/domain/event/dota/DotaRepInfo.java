package cn.gamemate.app.domain.event.dota;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DotaRepInfo implements Serializable
{
	static class DotaPlayer implements Comparable<DotaPlayer>{
		private final JsonNode playerNode;
		
		public DotaPlayer(JsonNode node) {
			this.playerNode = node;
		}
		
		public long getQuitTime(){
			return playerNode.get("time").getLongValue();
		}
		
		public String getLeaveResult(){
			return playerNode.get("leave_result").getTextValue();
		}
		
		public int getTeam(){
			return playerNode.get("team").getIntValue();
		}
		
		public String getName(){
			return playerNode.get("name").getTextValue();
		}
		
		public JsonNode get(String fieldName){
			return playerNode.get(fieldName);
		}

		@Override
		public int compareTo(DotaPlayer o) {
			long value0 = getQuitTime();
			long value1 = o.getQuitTime();
			if (value0 > value1) return 1;
			else if(value0 < value1 ) return -1;
			else return 0;
		}
		
	};
	
	private static final long serialVersionUID = 1L;
	/*
	 * Only support 2 teams
	 */
	private static final int [] teamList = {0,1};
	
	private static final Logger logger = LoggerFactory.getLogger(
		"cn.gamemate.app.domain.event.dota");
	
	Integer version;
	String rep_file_name;
	String submitter_name;
	int submitter_id;
	String cdp_output;
	String text;
	JsonNode cdp_output_dom;
	int upload_time;
	//TODO: 这里outcome 和 text 都是从python解析的数据里直接读出来的。也许都用java实现会更加一致。
	
	final List<Float> outcome = new ArrayList<Float>(2);
	final List<DotaPlayer> players = new ArrayList<DotaPlayer>(10);
	
	public List<DotaPlayer> getPlayers() {
		return players;
	}

	private DotaRepInfo(){};
	
	//- Properties ----------------------------------

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getRep_file_name() {
		return rep_file_name;
	}

	public void setRep_file_name(String rep_file_name) {
		this.rep_file_name = rep_file_name;
	}

	public String getSubmitter_name() {
		return submitter_name;
	}

	public void setSubmitter_name(String submitter_name) {
		this.submitter_name = submitter_name;
	}

	public int getSubmitter_id() {
		return submitter_id;
	}

	public void setSubmitter_id(int submitter_id) {
		this.submitter_id = submitter_id;
	}

	public String getCdp_output() {
		return cdp_output;
	}

	public void setCdp_output(String cdp_output) {
		this.cdp_output = cdp_output;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public int getUpload_time(){
		return upload_time;
	}
	
	public void setUpload_time(int value){
		this.upload_time = value;
	}

	//- Methods -----------------------------
	
	
	private void init(){
		for (int teamid: teamList){
			Iterator<JsonNode> teams = cdp_output_dom.get("teams").get(teamid).getElements();
			while(teams.hasNext()){
				JsonNode node = teams.next();
				players.add(new DotaPlayer(node));
			}
			Collections.sort(players);
		}
	}
	
	public String toJson(){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static DotaRepInfo fromJson(String json){
		ObjectMapper mapper = new ObjectMapper();
		try {
			DotaRepInfo value = mapper.readValue(json, DotaRepInfo.class);
			value.cdp_output_dom = mapper.readTree(value.cdp_output);
			value.init();
			return value;
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取有效游戏时间。为从游戏开始到战斗的一方全部退出或者一方基地被推倒的时间。
	 * @return Effective Play Time in miliseconds.
	 */
	public long getEffectivePlayTime(){
		
		
		long result = 0;
		
		int [] memberCount = new int[teamList.length];
		for (int teamid: teamList){
			
			Iterator<JsonNode> teams = cdp_output_dom.get("teams").get(teamid).getElements();
			while(teams.hasNext()){
				teams.next();
				memberCount[teamid]+=1;
			}
		}
		
		//calculate the time when all members of one forces all quit.
		outer:for (DotaPlayer player: players){
			//String leaveResult = player.get("leave_result").getTextValue();
			//if (leaveResult.equals("Left") || leaveResult.equals("Disconnect")){
			memberCount[player.get("team").getIntValue()] -=1;
			//		}
			for (int c : memberCount){
				if (c == 0){
					result = player.get("time").getLongValue();
					break outer;
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取完整的游戏时间。为从游戏开始到此Replay结束的时间。
	 * @return Full Play Time in miliseconds.
	 */
	public long getFullPlayTime(){
		return cdp_output_dom.get("header").get("length").getLongValue();
	}
	
	/**
	 * 游戏是否是最终推塔结束
	 */
	public boolean isFinished(){
		if (cdp_output_dom.get("extra").get("parsed_winner")!=null){
			return true;
		}
		return false;
	}
	
	private int [] getTeamMemberCount(){
		int [] memberCount = new int[teamList.length];
		for (int teamid: teamList){
			
			Iterator<JsonNode> teams = cdp_output_dom.get("teams").get(teamid).getElements();
			while(teams.hasNext()){
				teams.next();
				memberCount[teamid]+=1;
			}
		}
		return memberCount;
	}
	
	public boolean isQuit() {
		int[] teamMemberCount = getTeamMemberCount();
		List<DotaPlayer> quitPlayers = getQuitPlayers();
		for (DotaPlayer player: quitPlayers){
			int team = player.getTeam();
			teamMemberCount[team] -=1;
			if (teamMemberCount[team] == 0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取游戏胜利方的BattleForce ID
	 * @see #getOutcome()
	 * @return winner's force ID or null if no one wins.
	 */
	public Integer getWinnerForceId(){
		Integer j=null;
		float m=Float.MAX_VALUE;
		for (int i =0;i<outcome.size();i++){
			if (outcome.get(i).intValue() < m){
				j=i;
				m=outcome.get(i).intValue();
			}
		}
		return j;
	}
	
	/**
	 * 获取游戏结果. 
	 * 
	 * @see #getWinnerForceId()
	 * @return 
	 */
	public List<Float> getOutcome(){
		return outcome;
	}
	
	/**
	 * 获取玩家人数。
	 * @return
	 */
	public int getPlayerCount(){
		return players.size();
	}
	
	/**
	 * 获取在指定时间之前退出的玩家
	 * @param beforeTimeInMiliseconds time in miliseconds
	 */
	public List<DotaPlayer> getQuitPlayers(int beforeTimeInMiliseconds){

		List<DotaPlayer> result = new ArrayList<DotaPlayer>();
		
		//replay提交着的推出时间。没有人会在这个时间之后。
		long myQuitTime = 0; 
		for (DotaPlayer player: players){
			if (player.getQuitTime() > myQuitTime){
				myQuitTime = player.getQuitTime();
			}
		}
		
		//calculate the time when all members of one forces all quit.
		for (DotaPlayer player: players){
			
			//在我之后退出
			if (player.getQuitTime() == myQuitTime){
				continue;
			}
			
			String leaveResult = player.getLeaveResult();
			if (player.getQuitTime() > beforeTimeInMiliseconds){
				break;
			}
			
			if (leaveResult == null){
				logger.debug("leave result not string, {}", player.get("leave_result").getIntValue());
			}
			
			if (leaveResult == null || "Left".equals(leaveResult) || "Disconnect".equals(leaveResult) ){
				result.add(player);
			}
		}
		return result;
	}
	
	public List<DotaPlayer> getQuitPlayers(){
		return getQuitPlayers(Integer.MAX_VALUE);
	}

		
}
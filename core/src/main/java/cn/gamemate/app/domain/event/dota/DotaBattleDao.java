package cn.gamemate.app.domain.event.dota;

public interface DotaBattleDao {
	
	DotaRepInfo getReplayInfo(String battleId, Integer submitterId);

}

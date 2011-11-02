package cn.gamemate.app.domain.event.dota;

public interface DotaRepProcessingService {
	
	void postProcess(String BattleId, int submitter);
}

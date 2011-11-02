package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.Team;
import cn.gamemate.app.domain.user.User;

public interface PlayerSubmitResultSupport {
	
	void playerSubmitResult(User player, RtsTeam team, Integer battleId, Integer winnerForce,byte[] pr);
	
}

package cn.gamemate.app.domain.event.rts;

import cn.gamemate.app.domain.user.User;

public interface RefereeJudgementSupport {
	
	void refereeJudge(User referee, int winnerForce);
	
}

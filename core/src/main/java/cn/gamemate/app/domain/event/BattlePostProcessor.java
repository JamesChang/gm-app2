package cn.gamemate.app.domain.event;

import cn.gamemate.app.domain.arena.Battle;

interface BattlePostProcessor {
	
	class ProcessingException extends Exception{
		
	}
	
	void postProcess(Battle battle) throws ProcessingException;
	
	String getDescription();

}

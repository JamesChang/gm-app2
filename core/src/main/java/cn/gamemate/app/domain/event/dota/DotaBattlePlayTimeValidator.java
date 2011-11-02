package cn.gamemate.app.domain.event.dota;

import cn.gamemate.common.Times;

/**
 * 要求游戏的有效游戏时间必须足够长。
 * Thread Safe.
 * @author James Chang
 *
 */
public class DotaBattlePlayTimeValidator implements DotaRepProcessor{
	
	private final int minimumPlayTime;
	
	/**
	 * Short description. 
	 */
	private final String desc;
	
	public DotaBattlePlayTimeValidator(int minimumPlayTime) {
		this.minimumPlayTime  = minimumPlayTime;
		desc = "有效游戏时间大于" + Times.seconds2CN_short(minimumPlayTime);
	}
	
	@Override
	public String getDescription() {
		return desc;
	}
	
	@Override
	public void postProcess(DotaRepInfo repInfo) throws ProcessingException {
		if (repInfo.getEffectivePlayTime()/1000 <  minimumPlayTime){
			throw new ProcessingException(desc);
		}
	}

}

package cn.gamemate.app.domain.event.dota;

public class DotaBattlePlayerCountValidator  implements DotaRepProcessor{

	/**
	 * 最少总游戏人数
	 */
	private final int minTotalPlayerCount;
	private final int earlyQuitThreshold = 60 * 3 * 1000; 
	
	/**
	 * Short description. 
	 */
	private final String desc;
	
	public DotaBattlePlayerCountValidator(int minTotalPlayerCount) {
		this.minTotalPlayerCount = minTotalPlayerCount;
		desc = "最少有效游戏人数" + minTotalPlayerCount + "人";
	}
	
	@Override
	public void postProcess(DotaRepInfo repInfo) throws ProcessingException {
		
		int playerCount = repInfo.getPlayerCount();
		int earlyQuitCount = repInfo.getQuitPlayers(earlyQuitThreshold).size();
		
		if ( (playerCount - earlyQuitCount)< minTotalPlayerCount){
			throw new ProcessingException(desc);
		}
		
	}

	@Override
	public String getDescription() {
		return desc;
	}
}

package cn.gamemate.app.domain.event.dota;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.event.awards.AwardsPackage;
import cn.gamemate.app.domain.event.dota.cass.CassandraDotaBattleDao;


/**
 * 临时性的实现
 * 
 * 用来配置给每个赛事的实现。
 * 
 * 
 * @author jameszhang
 *
 */
public class ConfigurableDefaultDataProcessingService 
	extends DefaultDotaRepProcessingService{
	
	public ConfigurableDefaultDataProcessingService(CassandraDotaBattleDao dao) {
		super(dao);
	}


	private Map<Integer, List<AwardsPackage>> eventAwardsPackages = 
		new HashMap<Integer, List<AwardsPackage>>(); 
	
	public void setEventAwardsPackages(Map<Integer, List<AwardsPackage>> arg){
		this.eventAwardsPackages = arg;
		
	}
	
	@Override
	protected Iterable<AwardsPackage> getAwardsPackages(Battle battle) {
		
		String id = battle.getArenaSnapshot().getEventId();
		System.out.println("event id = " + id);
		List<AwardsPackage> list = eventAwardsPackages.get(Integer.valueOf(id));
		if (list == null){
			list = Collections.EMPTY_LIST;
		}
		return list;
	}

}

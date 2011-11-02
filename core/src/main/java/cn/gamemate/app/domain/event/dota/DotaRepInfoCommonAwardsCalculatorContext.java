package cn.gamemate.app.domain.event.dota;

import java.io.IOException;
import java.util.Iterator;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proto.res.ResArena;
import proto.util.Util.StringDictItem;
import cn.gamemate.app.domain.event.awards.AwardsCalculatorContext;
import cn.gamemate.app.domain.event.awards.BattleAwards;
import cn.gamemate.app.domain.event.awards.CommonAwardsCalculator;

/**
 * 根据Dota的RepInfo 计算胜负统计
 * @author jameszhang
 *
 */
public class DotaRepInfoCommonAwardsCalculatorContext extends AwardsCalculatorContext{
	
	
	private DotaRepInfo repInfo;
	private DotaBattle battle; 
	
	class InnerIterator implements Iterator<Integer>{
		
		private ResArena.ArenaSlot temp;
		private int winnerForceId;
		private Iterator<ResArena.ArenaSlot> iterator;
		public InnerIterator(int winnerForceId, Iterator<ResArena.ArenaSlot> iter) {
			this.winnerForceId = winnerForceId;
			this.iterator = iter;
		}

		private void _next(){
			while(temp == null && iterator.hasNext()){
				ResArena.ArenaSlot o = iterator.next();
				if (o.getUser()!= null && o.getUser().getId() >0 && o.getForceID() == winnerForceId){
					temp = o;
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			_next();
			if (temp == null){
				return false;
			}else{
				return true;
			}
		}

		@Override
		public Integer next() {
			_next();
			ResArena.ArenaSlot r = temp;
			temp = null;
			return r.getUser().getId();
		}

		@Override
		public void remove() {
			throw new RuntimeException();
		}
	};
	
	
	public DotaRepInfoCommonAwardsCalculatorContext(DotaBattle battle, DotaRepInfo repInfo) {
		super();
		this.repInfo = repInfo;
		this.battle = battle;
	}
	
	
	@Override
	public Iterator<Integer> getWinners(){
		final int winnerForceId = repInfo.getWinnerForceId();
		final Iterator<ResArena.ArenaSlot> iterator = battle.getArenaSnapshot().getPlayersList().iterator();
		return new InnerIterator(winnerForceId, iterator);
		
	}
	
	@Override
	public Iterator<Integer> getLossers(){
		final int winnerForceId = repInfo.getWinnerForceId();
		final Iterator<ResArena.ArenaSlot> iterator = battle.getArenaSnapshot().getPlayersList().iterator();
		return new InnerIterator(1 - winnerForceId, iterator);
	}

}

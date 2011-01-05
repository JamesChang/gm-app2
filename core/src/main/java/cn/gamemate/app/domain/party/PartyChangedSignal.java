package cn.gamemate.app.domain.party;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelEvent;

public class PartyChangedSignal extends DomainModelEvent {
	public PartyChangedSignal(DomainModel model) {
		super(model);
	}
	
	@Override
	public DefaultParty getModel(){
		return (DefaultParty)model;
	}
	
}

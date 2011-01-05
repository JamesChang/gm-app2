package cn.gamemate.app.domain.party;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelEvent;

public class PartyClosedSignal extends DomainModelEvent {

	public PartyClosedSignal(DomainModel model) {
		super(model);
	}
	
	@Override
	public DefaultParty getModel(){
		return (DefaultParty)model;
	}
	

}

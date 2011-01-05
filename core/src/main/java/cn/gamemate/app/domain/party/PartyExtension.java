package cn.gamemate.app.domain.party;

import cn.gamemate.app.domain.DomainModelEvent;
import cn.gamemate.app.domain.DomainModelExtension;

public class PartyExtension extends DomainModelExtension{
	@Override
	public void handleEvent(DomainModelEvent e) {
		 if (e instanceof PartyClosedSignal) {
			 closed((PartyClosedSignal)e);
		 }
		 else if (e instanceof PartyChangedSignal){
			 partyChanged((PartyChangedSignal)e);
		 }
	}
	public void closed(PartyClosedSignal e){
		
	}
	
	public void partyChanged(PartyChangedSignal e){
		
	}
	
	
}

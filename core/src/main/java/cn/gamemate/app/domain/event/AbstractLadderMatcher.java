package cn.gamemate.app.domain.event;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractLadderMatcher implements LadderMatcher{
	
	protected final List<MatcherHandler> handlers ;
	
	public AbstractLadderMatcher(){
		handlers = new ArrayList<MatcherHandler>();
	}

	synchronized public void addHandler(MatcherHandler handler){
		handlers.add(handler);
	}
	
	protected void fireMatched(List<Party> parties1, List<Party> parties2){
		for(MatcherHandler handler: handlers){
			MatcherContext ctx = new MatcherContext();
			ctx.force1Parties= parties1;
			ctx.force2Parties= parties2;
			handler.onMatched(ctx);
		}
	}

}

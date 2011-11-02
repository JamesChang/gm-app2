package cn.gamemate.app.domain.event;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResEvent;

@RooJavaBean
@Entity
@RooEntity
@DiscriminatorValue("eli_match")
public class EliminationMatch extends EntityEvent{
	
	@Basic
	@NotNull
	protected Integer requiredWin;
	
	@Basic
	protected Integer maxRound;
	
	@OneToOne
	protected EventForce home;
	
	@OneToOne
	protected EventForce away;
	
	@Basic
	protected float homeScore = 0.0f;
	
	@Basic
	protected float awayScore = 0.0f;
	
	@Override
	public ResEvent.EventGet.Builder toProtobuf(int verbose) {
		
		ResEvent.EventGet.Builder builder = super.toProtobuf(verbose);
		builder.setRequiredWin(requiredWin);
		builder.setHomeScore(homeScore);
		builder.setAwayScore(awayScore);
		if (home!=null){
			builder.setHome(home.toProtobuf());
		}
		if (away!=null){
			
			builder.setAway(away.toProtobuf());
		}
		
		
		return builder;
		
	}
	

	


}



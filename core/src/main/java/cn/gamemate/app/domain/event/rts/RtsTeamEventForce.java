package cn.gamemate.app.domain.event.rts;

import java.util.Collection;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResEvent;
import proto.response.ResEvent.EventForce.Builder;

import com.google.common.collect.ImmutableList;

import cn.gamemate.app.domain.Team;
import cn.gamemate.app.domain.event.EventForce;
import cn.gamemate.app.domain.user.User;
@Entity
@RooEntity
@RooJavaBean
@DiscriminatorValue("rtsteam")
public class RtsTeamEventForce extends RtsEventForce{
	
	@ManyToOne
	private RtsTeam team;
	
	public RtsTeamEventForce(RtsTeam team) {
		this.team = team;
	}
	
	@Override
	@Transient
	public Collection<User> getPlayers() {
		return team.getPlayers();
	}

	@Override
	@Transient
	public String getName() {
		return team.getName();
	}
	
	@Override
	public Builder toProtobuf() {
		Builder builder = super.toProtobuf();
		builder.setTeam(team.toProtobuf());
		builder.setName(team.getName()).setImage(team.getImage()).setId(team.getId());
		builder.setChecked(isChecked());
		return builder;
	}
	
	@Override
	public String getForceType() {
		return "team";
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof RtsTeamEventForce)) return false;
		RtsTeamEventForce obj2 = (RtsTeamEventForce) obj;
		return this.team==null ? obj2.team==null : this.team.equals(obj2.team);
	}
	
	@Override
	public int hashCode() {
		return team==null ? 0 : team.hashCode();
	}
	
	@Override
	public void attatchExtraInfo(Builder builder,
			List<String> requiredPlayerInfo) {
		if (requiredPlayerInfo==null){return;}
		super.attatchExtraInfo(builder, requiredPlayerInfo);
		for (String fieldName : requiredPlayerInfo){
			User user = team.getLeader();
			if (user==null){continue;}
			proto.response.ResEvent.DynamicField.Builder builder3 = 
				ResEvent.DynamicField.newBuilder()
				.setName(fieldName);
			String valueFromUser = PlayerInfoChecker.getValueFromUser(user, fieldName);
			if (valueFromUser == null){continue;}
			builder3.setValue(valueFromUser);
			builder.addExtraFields(builder3);
		}
		
	}

}

package cn.gamemate.app.domain.event;

import java.util.Collection;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.roo.addon.entity.RooEntity;

import cn.gamemate.app.domain.Team;

@Entity
@RooEntity
@DiscriminatorValue("team")
public class EventTeamForce extends EventForce {
	
	@Override
	public String getForceType() {
		return "team";
	}
}

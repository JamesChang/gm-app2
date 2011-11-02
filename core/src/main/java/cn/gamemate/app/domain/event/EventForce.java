package cn.gamemate.app.domain.event;

import java.util.Collection;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;
import javax.persistence.Column;
import javax.persistence.ManyToMany;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import com.google.common.collect.Lists;

import proto.response.ResEvent;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.event.rts.RtsElimination;
// 实际上这是一个Value Object.


@Entity
@RooEntity
@RooJavaBean
@Inheritance
@DiscriminatorColumn(name = "force_type")
@DiscriminatorValue("base")
public class EventForce implements DomainModel{
	
	@Column(name="force_type", insertable=false, updatable = false)
	private String forceType;
	
	//This is wrong
	//@ManyToMany(mappedBy="registeredForces")
	//List<RtsElimination> registeredEvents;
	
	@Transient
	public Collection<User> getPlayers(){
		return null;
	}
	@Transient
	public String getName(){
		return null;
	}
	@Override
	public ResEvent.EventForce.Builder toProtobuf() {
		ResEvent.EventForce.Builder builder = 
			ResEvent.EventForce.newBuilder().setName(getName());
		builder.setType(this.getForceType());
		if (this.getId()!=null){
			builder.setForceId(getId().intValue());
		}
		return builder;
	}
	public String getForceType() {
		return "base";
	}
	
	public void attatchExtraInfo(ResEvent.EventForce.Builder builder, List<String> requiredPlayerInfo){
	}
	
	
}

package cn.gamemate.app.domain.event.rts;

import java.util.Collection;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResEvent;
import proto.response.ResEvent.EventForce.Builder;
import cn.gamemate.app.domain.event.EventForce;
import cn.gamemate.app.domain.user.User;

import com.google.common.collect.ImmutableList;

@Entity
@RooEntity
@RooJavaBean
@DiscriminatorValue("user")
public class RtsSingleUserEventForce extends RtsEventForce	{
	
	@Transient
	List<User> users;
	
	@ManyToOne
	private User user;
	
	public RtsSingleUserEventForce(User user) {
		this.user = user;
	}

	@Override
	@Transient
	public Collection<User> getPlayers() {
		if (users == null){
			synchronized (this) {
				users = ImmutableList.of(user);
			}
		}
		return users;
	}

	@Override
	@Transient
	public String getName() {
		return user.getName();
	}
	
	@Override
	public String getQQ(){
		return user.getQq();
	}
	
	@Override
	public Builder toProtobuf() {
		Builder builder = super.toProtobuf();
		builder.setUser(user.toProtobuf());
		builder.setName(user.getName());
		builder.setImage(user.getPortrait());
		builder.setId(Integer.toString(user.getId()));
		if (getQQ()!=null){
			builder.setImQq(getQQ());
		}
		builder.setChecked(isChecked());
		if (isQuit()){
			builder.setQuit(isQuit());
		}
		return builder;
	}
	
	@Override
	public String getForceType() {
		return "user";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof RtsSingleUserEventForce)) return false;
		RtsSingleUserEventForce obj2 = (RtsSingleUserEventForce) obj;
		return this.user==null ? obj2.user==null : this.user.equals(obj2.user);
	}
	
	@Override
	public int hashCode() {
		return user==null ? 0 : user.hashCode();
	}
	
	public List<RtsElimination> myRegisteredEvents(){
		Query query = entityManager().createQuery("SELECT event FROM RtsElimination event JOIN event.registeredForces force " + 
				"WHERE force.user=:user ", RtsElimination.class);
		query.setParameter("user", user);
		List<RtsElimination> resultList = query.getResultList();
		return resultList;
	}
	
	@Override
	public void attatchExtraInfo(Builder builder,
			List<String> requiredPlayerInfo) {
		if (requiredPlayerInfo==null){return;}
		super.attatchExtraInfo(builder, requiredPlayerInfo);
		for (String fieldName : requiredPlayerInfo){
			proto.response.ResEvent.DynamicField.Builder builder3 = ResEvent.DynamicField.newBuilder()
			.setName(fieldName);
			String valueFromUser = PlayerInfoChecker.getValueFromUser(user, fieldName);
			if (valueFromUser == null){continue;}
			builder3.setValue(valueFromUser);
			builder.addExtraFields(builder3);
		}
		
	}
	
}

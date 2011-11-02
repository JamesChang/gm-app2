package cn.gamemate.app.domain.event.rts;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResEvent.EventForce.Builder;

import com.google.common.collect.ImmutableList;

import cn.gamemate.app.domain.event.EventForce;
import cn.gamemate.app.domain.user.User;

@Entity
@RooEntity
@RooJavaBean
@DiscriminatorValue("rtsbase")
public class RtsEventForce extends EventForce{
	
	
	
	private static RtsEventForce quitForce = new RtsQuitEventForce();
	public static RtsEventForce getQuitForce(){
		return quitForce;
	}
	
	@Basic
	private Integer bestRound;
	
	@Basic
	private Integer nextMatchId;
	
	@Basic
	private boolean quit = false;
	
	@Basic
	private boolean checked=false;

	public String getQQ(){
		return null;
	}

}

class RtsQuitEventForce extends RtsEventForce{
	@Override
	@Transient
	public String getName() {
		return "弃权";
	}
	public Long getId() {
		return 0l;
	};
	public boolean isQuit() {return true;}
	
	@Override
	public Builder toProtobuf() {
		Builder builder = super.toProtobuf();
		builder.setName(getName());
		builder.setChecked(isChecked());
		if (isQuit()){
			builder.setQuit(isQuit());
		}
		return builder;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj instanceof RtsQuitEventForce){
			return true;
		}else{
			return false;
		}
		
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
}

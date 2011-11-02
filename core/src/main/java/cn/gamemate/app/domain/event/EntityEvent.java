package cn.gamemate.app.domain.event;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import proto.response.ResEvent;
import cn.gamemate.app.domain.event.rts.RtsEventForceType;
import cn.gamemate.app.domain.event.rts.RtsSingleUserEventForce;



@RooJavaBean
@Entity
@RooEntity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="event_type")
@DiscriminatorValue("base")
public class EntityEvent extends Event{
	

	public String getEventType(){
		return "Base";
	}
	
    @Override
	public Integer getId() {
		return id;
	}
    public Integer setId(Integer id) {
		return this.id= id;
	}
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /*@Basic
    @NotNull
    private EventType eventType;*/
    

    @Basic
	@NotNull
    private String name;

    @Basic
	private String mode;
    

	@Basic
	@Column(name="parent_id")
	protected Integer parentId;
    
	@Override
	public ResEvent.EventGet.Builder toProtobuf(int verbose) {
		ResEvent.EventGet.Builder builder = ResEvent.EventGet.newBuilder();
		builder.setName(name).setId(id);
		builder.setType(getEventType());
		if (parentId!=null){ builder.setParentId(parentId);}
		return builder;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof EntityEvent)) return false;
		EntityEvent obj2 = (EntityEvent) obj;
		return this.id==null ? false : this.id.equals(obj2.id);
	}
	

	@Override
	public int hashCode() {
		return id==null ? 0 : id.hashCode();
	}

}

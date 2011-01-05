package cn.gamemate.app.domain.user;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.safehaus.uuid.UUID;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cn.gamemate.app.domain.DomainModelRuntimeException;

import com.google.common.base.Objects;

import proto.response.ResUser;

@Entity
@RooJavaBean
@RooEntity
@Table(name = "gm_user")
@NamedQueries({ @NamedQuery(name = "findByName", query = "SELECT o from User o WHERE o.name = :name") })
public class User implements UserDetails {

	public enum UserStatus {
		OFFLINE, DROP, BROWSING, ONLINE, GAMING
	};

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NotNull
	private String name;

	@NotNull
	private String portrait;

	@NotNull
	private String password;

	@NotNull
	@Column(name = "dynamic_status")
	private UserStatus status;

	@NotNull
	@Column
	private int gold;

	// TODO: concurrent access
	transient private Integer arenaId;
	transient private Integer eventId;
	transient private UUID partyId;
	transient private String statusEx = "";

	public String getStatusEx() {
		return statusEx;
	}

	public void setStatusEx(String statusEx) {
		this.statusEx = statusEx;
	}

	public Integer getArenaId() {
		return this.arenaId;
	}

	public void setArenaId(Integer arenaId) {
		this.arenaId = arenaId;
	}

	public synchronized Integer getEventId() {
		return eventId;
	}

	public synchronized boolean isInEvent() {
		return eventId == null;
	}

	public synchronized void casEventId(Integer newEventId) {
		if (eventId != null) {
			throw new DomainModelRuntimeException("eventId incorrect");
		}
		eventId = newEventId;
	}

	public synchronized void cacEventId(Integer oldEventId) {
		if (eventId != oldEventId) {
			throw new DomainModelRuntimeException("EventId incorrect");
		}
		this.eventId = null;
	}

	public synchronized UUID getPartyId() {
		return partyId;
	}

	public synchronized void casPartyId(UUID newPartyId) {
		if (this.partyId != null) {
			throw new DomainModelRuntimeException("partyid incorrect");
		}
		partyId = newPartyId;
	}

	public synchronized void cacPartyId(UUID partyId) {
		if (this.partyId != partyId) {
			throw new DomainModelRuntimeException("partyid incorrect");
		}
		this.partyId = null;
	}

	public synchronized boolean isInParty() {
		return partyId != null;
	}

	/**
	 * @return the status
	 */
	public UserStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User(int id, String name, String portrait) {
		super();
		this.id = id;
		this.name = name;
		this.portrait = portrait;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public static User findArenaUserByName(String name) {
		return (User) entityManager().createNamedQuery("findByName")
				.setParameter("name", name).getSingleResult();
	}

	// TODO: move this to AJ file
	public ResUser.UserModel.Builder toProtobuf() {
		ResUser.UserModel.Builder builder = ResUser.UserModel.newBuilder();
		copyTo(builder);
		return builder;
	}

	public void copyTo(ResUser.UserModel.Builder builder) {
		builder.setName(name).setId(id).setPortrait(portrait);
		builder.setStatus(status.name().toLowerCase());
		builder.setIsInArena(arenaId == null ? false : true);
		builder.setIsInParty(partyId == null ? false : true);
		builder.setIsInGroupingList((eventId != null && eventId > 3 && arenaId == null) ? true : false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User o = (User) obj;
			if (o != null && o.id == this.id) {
				return true;
			} else {
				return false;
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.id;
	};

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return new ArrayList<GrantedAuthority>();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("id", getId())
				.add("name", name).toString();
	}

}

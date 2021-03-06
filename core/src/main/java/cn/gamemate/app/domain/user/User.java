package cn.gamemate.app.domain.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

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

/**
 * @author jameszhang
 *
 */
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
	private String email;

	@NotNull
	private String portrait;

	@NotNull
	private String password;

	@NotNull
	@Column(name = "dynamic_status")
	private UserStatus status;
	

	@Column(name = "hf_name")
	private String hfName;
	

	@Column(name = "sjtubn")
	private String sjtubn;
	
	@Column(name = "vs_name")
	private String vsName;

	@Column(name = "im_qq")
	private String qq;
	
	
	private String mobile;
	
	
	@Column(name = "chineseId")
	private String chineseId;
	
	private String address;

	private String truename;
	
	@NotNull
	@Column
	private int gold;

	// TODO: concurrent access
	transient private Integer arenaId;
	
	transient private UUID partyId;
	transient private String statusEx = "";
	transient public String relayService = null;
	
	public synchronized boolean casRelayServiceName(String oldName, String newName){
		if ((relayService == null && oldName == null) ||
			(relayService != null && relayService.equals(oldName))){
			relayService = newName;
			return true;
		}else{
			throw new DomainModelRuntimeException("refuse to clear user's relayService field.");
		}
	}
	public synchronized boolean setRelayServiceName(String newName){
		relayService = newName;
		return true;
	}
	public synchronized String getRelayServiceName(){
		return relayService;
	}
	

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

	transient private final AtomicInteger eventId = new AtomicInteger();
	
	public int getEventId() {
		return eventId.get();
	}

	public boolean isInEvent() {
		return eventId.get() != 0;
	}

	public void casEventId(int oldEventId, int newEventId) {
		if (!eventId.compareAndSet(oldEventId, newEventId)) {
			throw new DomainModelRuntimeException("eventId incorrect");
		}
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
		if (status.equals(UserStatus.ONLINE) && getRelayServiceName() == null){
			return UserStatus.BROWSING;
		}
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
		builder.setStatus(getStatus().name().toLowerCase());
		builder.setIsInArena(arenaId == null ? false : true);
		builder.setIsInParty(partyId == null ? false : true);
		if (getArenaId()!=null){
			builder.setCampusArenaID(getArenaId());
		}
		int e = eventId.get();
		builder.setIsInGroupingList((e > 3 && arenaId == null) ? true : false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this==obj){return true;}
		if (!(obj instanceof User)){return false;}
		User o = (User) obj;
		return this.id == o.id;
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

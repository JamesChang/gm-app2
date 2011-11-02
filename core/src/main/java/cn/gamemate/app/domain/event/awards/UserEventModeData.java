package cn.gamemate.app.domain.event.awards;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import cn.gamemate.app.domain.user.User;


@SuppressWarnings("serial")
class UserEventModeDataPK implements Serializable {

	int eventID;

	int userID;
	
	@NotNull
	String mode;

	public UserEventModeDataPK() {
	}

	public UserEventModeDataPK( int userID,int eventID, String mode) {
		super();
		this.eventID = eventID;
		this.mode = mode;
		this.userID = userID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserEventModeDataPK) {
			final UserEventModeDataPK other = (UserEventModeDataPK) obj;
			return eventID == other.eventID && userID == other.userID &&
				 mode.equals(other.mode);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return eventID ^ userID ^ mode.hashCode();
	}

}


@RooToString
@Entity
@Table(name="gm_user_event_mode")
@Configurable
@IdClass(UserEventModeDataPK.class)
public class UserEventModeData extends BattlePlayerAwards{

    @PersistenceContext
    transient EntityManager entityManager;
    

    @Id
    @Column(name = "event_id")
    private int eventID;
    
    
    @Id
    @Column(name = "user_id")
    private int userID;
    
    @Id
	String mode;
    
    
    @Version
    @Column(name = "version")
    private Integer version = 1;
    
    
    @Column(name = "_sa_power")
    public int power = 0;
    

	public Integer gold = 0;
	public Integer activity = 0;
	public double trueskill_mean = 0.0;
	public double trueskill_sd = 0.0;
	public int total = 0;
	public int win = 0;
	
	//RTS
	public int first = 0;
	public int second = 0;
	public int third = 0;
	public int rtsScore = 0;
	

    public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	

    public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }

    public static final EntityManager entityManager() {
        EntityManager em = new UserEventModeData().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static UserEventModeData find(int userId, int eventId, String mode) {
        return entityManager().find(UserEventModeData.class, new UserEventModeDataPK(userId, eventId, mode));
    }
    
    public static List<UserEventModeData> findForLeaderBoard(int firstResult, int maxResults, int eventId, String mode) {
        return entityManager().createQuery("SELECT o FROM UserEventModeData o where o.mode = :mode and o.eventID = :eventId order by o.rtsScore DESC ", UserEventModeData.class)
        	.setFirstResult(firstResult)
        	.setMaxResults(maxResults)
        	.setParameter("mode", mode).setParameter("eventId", eventId)
        	.getResultList();
        
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
}

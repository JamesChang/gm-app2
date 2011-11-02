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

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;


@SuppressWarnings("serial")
class UserEventDataPK implements Serializable {

	int eventID;

	int userID;

	public UserEventDataPK() {
	}

	public UserEventDataPK( int userID,int eventID) {
		super();
		this.eventID = eventID;
		this.userID = userID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserEventDataPK) {
			final UserEventDataPK other = (UserEventDataPK) obj;
			return eventID == other.eventID && userID == other.userID;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return eventID ^ userID;
	}

}


@RooToString
@Entity
@Table(name="gm_user_event")
@Configurable
@IdClass(UserEventDataPK.class)
public class UserEventData {
	

    @PersistenceContext
    transient EntityManager entityManager;
    

    @Id
    @Column(name = "event_id")
    private int eventID;
    
    
    @Id
    @Column(name = "user_id")
    private int userID;
    
    
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
	public int draw = 0;
	public int rtsScore = 0;
	
	public BattlePlayerAwards toBattlePlayerAwards(){
		BattlePlayerAwards awards = new BattlePlayerAwards();
		awards.total = total;
		awards.win = win;
		awards.draw = draw;
		awards.rtsScore = rtsScore;
		return awards;
	}
    
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
	

    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
	

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
        	UserEventData attached = UserEventData.findUserEventData(this.userID, this.eventID);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    

    @Transactional
    public UserEventData merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UserEventData merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static final EntityManager entityManager() {
        EntityManager em = new UserEventData().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static UserEventData findUserEventData(int userId, int eventId) {
        return entityManager().find(UserEventData.class, new UserEventDataPK(userId, eventId));
    }
    
    public static List<UserEventData> findForLeaderBoard(int eventId) {
    	return entityManager().createQuery("SELECT o FROM UserEventData o where o.eventID = :eventId order by o.rtsScore DESC ", UserEventData.class)
    	.setParameter("eventId", eventId)
    	.getResultList();
    }
    
    
	
}

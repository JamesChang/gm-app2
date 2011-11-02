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


@SuppressWarnings("serial")
class TeamEventModeDataPK implements Serializable {

	int eventID;

	@NotNull
	String teamID;
	
	@NotNull
	String mode;

	public TeamEventModeDataPK() {
	}

	public TeamEventModeDataPK( String teamID,int eventID, String mode) {
		super();
		this.eventID = eventID;
		this.mode = mode;
		this.teamID = teamID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TeamEventModeDataPK) {
			final TeamEventModeDataPK other = (TeamEventModeDataPK) obj;
			return eventID == other.eventID && teamID.equals(other.teamID) &&
				 mode.equals(other.mode);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return eventID ^ teamID.hashCode() ^ mode.hashCode();
	}

}


@RooToString
@Entity
@Table(name="gm_user_event_mode")
@Configurable
@IdClass(TeamEventModeDataPK.class)
public class TeamEventModeData extends BattlePlayerAwards{

    @PersistenceContext
    transient EntityManager entityManager;
    

    @Id
    @Column(name = "event_id")
    private int eventID;
    
    
    @Id
    @Column(name = "user_id")
    private int teamID;
    
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

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
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
        EntityManager em = new TeamEventModeData().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static TeamEventModeData find(String teamId, int eventId, String mode) {
        return entityManager().find(TeamEventModeData.class, new TeamEventModeDataPK(teamId, eventId, mode));
    }
    
    public static List<TeamEventModeData> findForLeaderBoard(int firstResult, int maxResults, String mode) {
        return entityManager().createQuery("SELECT o FROM TeamEventModeData o where o.mode = :mode", TeamEventModeData.class)
        	.setFirstResult(firstResult)
        	.setMaxResults(maxResults)
        	.setParameter("mode", mode)
        	.getResultList();
        
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
}
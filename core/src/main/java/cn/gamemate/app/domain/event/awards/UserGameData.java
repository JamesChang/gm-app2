package cn.gamemate.app.domain.event.awards;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("serial")
class UserGameDataPK implements Serializable {

	int gameID;

	int userID;

	public UserGameDataPK() {
	}

	public UserGameDataPK( int userID,int gameID) {
		super();
		this.gameID = gameID;
		this.userID = userID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserGameDataPK) {
			final UserGameDataPK other = (UserGameDataPK) obj;
			return gameID == other.gameID && userID == other.userID;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return gameID ^ userID;
	}

}


@RooToString
@Entity
@Table(name="gm_user_game")
@Configurable
@IdClass(UserGameDataPK.class)
public class UserGameData extends BattlePlayerAwards{

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	@Column(name = "_sa_power")
    public int power = 0;
	                       

	public Integer gold = 0;
	public Integer activity = 0;
	public double trueskill_mean = 0.0;
	public double trueskill_sd = 0.0;
	public int total = 0;
	public int win = 0;
    
    @Id
    @Column(name = "game_id")
    private int gameID;
    
    
    @Id
    @Column(name = "user_id")
    private int userID;
    
    
    @Version
    @Column(name = "version")
    private Integer version = 1;
    
    @PersistenceContext
    transient EntityManager entityManager;
    

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
            UserGameData attached = UserGameData.findUserGameData(this.userID, this.gameID);
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
    public UserGameData merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UserGameData merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static final EntityManager entityManager() {
        EntityManager em = new UserGameData().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static UserGameData findUserGameData(int userId, int gameId) {
        return entityManager().find(UserGameData.class, new UserGameDataPK(userId, gameId));
    }
    
  /*  public static List<UserGameData> UserGameData.findUserGameDataEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from UserGameData o", UserGameData.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }*/
    
}

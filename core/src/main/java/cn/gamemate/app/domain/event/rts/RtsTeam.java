package cn.gamemate.app.domain.event.rts;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.collect.Lists;

import proto.response.ResTeam;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.Team;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.user.User;

@NamedQuery(
name = "findTeamByUserGame", 
query = "SELECT t FROM RtsTeam t JOIN t.leader u WHERE u = :user AND t.lgid = :gameid")
@RooJavaBean
@RooToString
@RooEntity
@Table(name="gm_team")
public class RtsTeam implements Team, DomainModel{

    @NotNull
    @Size(max = 40)
    private String name;

    @Column(name = "logo")
    @Size(max = 200)
    private String image;

    @Size(max = 200)
    private String slogan;

    @Size(max = 200)
    private String prefix;

    @Id
    private String uuid;
    
    @Column(name = "lgid")
    private Integer lgid;
    /*
    @ManyToMany
    @JoinTable(name = "rts_team_user")
    private List<User> users;*/
    
    @ManyToOne
    @JoinColumn(name="leader_id")
    private User leader;
    
    public static RtsTeam findRtsTeamByUserGame(User user, Integer gameId) {
    	 if (user == null ) return null;
         if (gameId == null ) return null;
         List<RtsTeam> resultList = entityManager().createNamedQuery("findTeamByUserGame")
         .setParameter("user", user)
         .setParameter("gameid", gameId)
         .getResultList();
         
         if (resultList.isEmpty()){
         	return null;
         }else if (resultList.size() == 1){
         	return resultList.get(0);
         }else{
         	throw new javax.persistence.NonUniqueResultException();
         }
    }

    public static RtsTeam findRtsTeamByUserGame(User user, Game game) {
        if (game == null ) return null;
        return findRtsTeamByUserGame(user, game.getId());
    }


	@Override
	public Collection getPlayers() {
		if (leader!=null){
			return Lists.newArrayList(leader);
		}else{
			return Lists.newArrayList();
		}
	}


	@Override
	public String getId() {
		return uuid;
	}

	@Override
	public ResTeam.TeamGetResponse.Builder toProtobuf() {
		return toProtobuf(99);
	}
	
	public ResTeam.TeamGetResponse.Builder toProtobuf(int verbose) {
		ResTeam.TeamGetResponse.Builder builder = ResTeam.TeamGetResponse.newBuilder();
		builder.setUuid(this.uuid).setName(name).setLogo(image);
		return builder;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {return false;}
		if (obj == this) {return true;}
		if (!(obj instanceof RtsTeam)){
			return false;
		}
		RtsTeam other = (RtsTeam) obj;
		return this.uuid.equals(other.uuid);
	}
	
	@Override
	public int hashCode() {
		return this.uuid.hashCode();
	}

}



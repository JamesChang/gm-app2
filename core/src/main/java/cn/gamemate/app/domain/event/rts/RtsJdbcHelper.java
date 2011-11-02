package cn.gamemate.app.domain.event.rts;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import cn.gamemate.app.domain.user.User;

@Repository
@Configurable
public class RtsJdbcHelper {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void init(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<RtsEliminationMatch> getPlayerUpcomingMatches(User user){
		List<Integer> list = this.jdbcTemplate.queryForList(
			"select e.id from entity_event e JOIN event_force f ON (e.home=f.id or e.away = f.id) and f.user = ? where e.status = 3",
			Integer.class,
			user.getId()
			);
		List<RtsEliminationMatch> result = Lists.newArrayList();
		for(Integer id:list){
			RtsEliminationMatch match = RtsEliminationMatch.findRtsEliminationMatch(id);
			result.add(match);
		}
		return result;
	}
	
	public List<RtsEliminationMatch> getPlayerUpcomingMatches(User user, Integer gameId){
		List<Integer> list = this.jdbcTemplate.queryForList(
			"select e.id from entity_event e JOIN event_force f ON (e.home=f.id or e.away = f.id) and f.user = ? where e.status = 3 and game_id = ? ",
			Integer.class,
			user.getId(), gameId
			);
		List<RtsEliminationMatch> result = Lists.newArrayList();
		for(Integer id:list){
			RtsEliminationMatch match = RtsEliminationMatch.findRtsEliminationMatch(id);
			result.add(match);
		}
		return result;
	}
	
	public List<RtsEliminationMatch> getPlayerFinishedMatches(User user, Integer gameId){
		List<Integer> list = this.jdbcTemplate.queryForList(
			"select e.id from entity_event e JOIN event_force f ON (e.home=f.id or e.away = f.id) and f.user = ? where e.status = 1 and game_id = ? LIMIT 10",
			Integer.class,
			user.getId(), gameId
			);
		List<RtsEliminationMatch> result = Lists.newArrayList();
		for(Integer id:list){
			RtsEliminationMatch match = RtsEliminationMatch.findRtsEliminationMatch(id);
			result.add(match);
		}
		return result;
	}

}

package cn.gamemate.app.domain.arena;


import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cn.gamemate.app.domain.user.User;

public class ArenaUserJdbcDao {

	private JdbcTemplate jdbcTemplate;

	public ArenaUserJdbcDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

	public void addUser(User user) throws DataAccessException {
		// TODO Auto-generated method stub

	}

	
	public User getUser(Integer id) throws DataAccessException {
		return this.jdbcTemplate.queryForObject(
				"SELECT id, name, portrait, status FROM gm_user WHERE id = ?", 
			new Object[]{id}, 
			new UserMapper());
	}

	
	public User getUser(String name) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void removeUser(User user) throws DataAccessException {
		// TODO Auto-generated method stub

	}
	
	private static final class UserMapper implements RowMapper<User>{

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			final User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setPortrait(rs.getString("portrait"));
			return user;
		}
		
	}
}

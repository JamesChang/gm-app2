package cn.gamemate.app.domain.event.rts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import proto.response.ResEvent.EventGet.Builder;

import cn.gamemate.app.domain.event.EventForce;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/rdbms.xml"})
@TransactionConfiguration(defaultRollback=false)
@Transactional
public class RtsEliminationMatchTest {
	
	@PersistenceContext
	EntityManager entityManager;
	
	
	//@Test
	public void testCreation(){
		
		RtsEliminationMatch match = new RtsEliminationMatch();
		
		match.setName("bb");
		match.setRequiredWin(3);
		
		User user1  = User.findUser(64);
		User user2  = User.findUser(67);
		
		RtsSingleUserEventForce homeForce = new RtsSingleUserEventForce(user1);
		user1.merge();
		homeForce.persist();
		RtsSingleUserEventForce awayForce = new RtsSingleUserEventForce(user2);
		awayForce.persist();
		match.setHome(homeForce);
		match.setAway(awayForce);
		
		match.setGameId(2);
		match.setMap(GameMap.findGameMap(90L));
		match.status =  RtsEliminationMatch.Status.OPEN;
		match.playGround = RtsLocation.newHf();
		match.persist();
		
		
		Builder builder = match.toProtobuf();
		
		//Add A dummy battle
		match.playerSubmitResult(user1, null, 0, 0,null);
		match.merge();
		
		//reload
		RtsEliminationMatch reloaded = RtsEliminationMatch.findRtsEliminationMatch(match.getId());
		String output = reloaded.toProtobuf().toString();
		
	}
	
	//@Test
	public void testSubmitResult(){
		RtsEliminationMatch reloaded = RtsEliminationMatch.findRtsEliminationMatch(1003);
		
		User user1  = User.findUser(64);
		
		User user2  = User.findUser(67);
		
		reloaded.playerSubmitResult(user1, null, 3, 1,null);
		reloaded.playerSubmitResult(user2, null, 3, 0,null);
		//reloaded.merge();
	}
	/*
	@Test
	public void testReSubmit(){
		RtsEliminationMatch reloaded = RtsEliminationMatch.findRtsEliminationMatch(1003);
		
		User user1  = User.findUser(64);
		
		User user2  = User.findUser(67);
		
		boolean b = entityManager.contains(reloaded);
		
		reloaded.playerSubmitResult(user1, null, 0, 1);
	}*/

	private RtsElimination newElimination(){
		RtsElimination match = new RtsElimination();
		
		match.setName("test elimination");
		match.setDefaultRequiredWin(3);
		match.setEventForceType(RtsEventForceType.USER);
		
		User user1  = User.findUser(64);
		User user2  = User.findUser(67);
		
		RtsSingleUserEventForce homeForce = new RtsSingleUserEventForce(user1);
		user1.merge();
		homeForce.persist();
		RtsSingleUserEventForce awayForce = new RtsSingleUserEventForce(user2);
		awayForce.persist();
		
		match.setGameId(2);
		match.setMap(GameMap.findGameMap(90L));
		match.setDefaultPlayGround(RtsLocation.newHf());
		return match;
		
	}
	
	//@Test
	public void testGenerateChildren2(){
		
		RtsElimination match = newElimination();
		match.generateChildren();
		match.persist();
		
		User user1  = User.findUser(64);
		User user2  = User.findUser(67);
		match.playerRegister(user1, null);
		match.playerRegister(user2, null);
		match.generateChildren();
	}
	
	//@Test
	public void testGenerateChildren8(){
		
		RtsElimination match = newElimination();
		match.generateChildren();
		match.persist();
		
		User user1  = User.findUser(64);
		User user2  = User.findUser(67);
		User user3  = User.findUser(70);
		User user4  = User.findUser(73);
		User user5  = User.findUser(76);
		User user6 = User.findUser(79);
		User user7  = User.findUser(82);
		User user8  = User.findUser(85);
		match.playerRegister(user1, null);
		match.playerRegister(user2, null);
		match.playerRegister(user3, null);
		match.playerRegister(user4, null);
		match.playerRegister(user5, null);
		match.playerRegister(user6, null);
		match.playerRegister(user7, null);
		match.playerRegister(user8, null);
		match.generateChildren();
	}
	
	//@Test
	public void testGenerateChildren7(){

		User user1  = User.findUser(64);
		User user2  = User.findUser(67);
		User user3  = User.findUser(70);
		User user4  = User.findUser(73);
		User user5  = User.findUser(76);
		User user6 = User.findUser(79);
		User flash90  = User.findUser(90);
		
		
		RtsElimination match = newElimination();
		match.generateChildren();
		match.setName("test elimination");
		match.setGameId(2);
		match.administrators.add(user1);
		match.administrators.add(flash90);
		match.persist();
		match.playerRegister(user1, null);
		match.playerRegister(user2, null);
		match.playerRegister(user3, null);
		match.playerRegister(user4, null);
		match.playerRegister(user5, null);
		match.playerRegister(user6, null);
		match.playerRegister(flash90, null);
		match.generateChildren();
	}
	
	//@Test
	public void testTeamElimination(){
		

		User user1  = User.findUser(64);
		User user2  = User.findUser(67);
		User user3  = User.findUser(70);
		User user4  = User.findUser(73);
		User user5  = User.findUser(76);
		user4.setHfName("aaa");
		
		RtsElimination match = newElimination();
		match.setEventForceType(RtsEventForceType.USER);
		match.setName("11111");
		match.setGameId(2);
		match.administrators.add(user1);
		match.setDefaultPlayGround(RtsLocation.newHf());
		match.setGameId(2);
		match.setMap(GameMap.findGameMap(90L));
		
		match.persist();
		match.playerRegister(user1, null);
		match.playerRegister(user2, null);
		match.playerRegister(user3, null);
		match.playerRegister(user4, null);
		match.playerRegister(user5, null);
		match.generateChildren();
		
	}
	//@Test
	public void testWe() throws Exception{
		User user1  = User.findUser(64);
		User user2  = User.findUser(67);
		
		RtsElimination match = newElimination();
		match.setEventForceType(RtsEventForceType.USER);
		match.setName("测试");
		//match.setRegisterationLimit(32);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = formatter.parse("2011-08-25 01:00");
		//match.setRegisterationDeadline(date);
		match.setGameId(1);
		match.administrators.add(user1);
		match.administrators.add(user2);
		match.setDefaultPlayGround(RtsLocation.newHf());
		match.setDefaultRequiredWin(2);
		match.setMap(GameMap.findGameMap(6L));
		match.setExpectedTime("2011-8-25----2011-8-27");
		match.persist();
		match.playerRegister(user1, null);
		match.playerRegister(user2, null);
		//match.playerRegister(user3, null);
		match.generateChildren();
		
		
		
		
	}
	
	//@Test
	public void testQuery(){
		User user1  = User.findUser(64);
		
		TypedQuery<RtsElimination> query = entityManager.createQuery(
				"SELECT event FROM RtsElimination event JOIN event.administrators admin " +
				"WHERE admin = :user", RtsElimination.class);
		query.setParameter("user", user1);
		List<RtsElimination> resultList = query.getResultList();
		
		System.out.println(resultList);
		
	}
	//@Test
	public void testQuery2(){
		User user1  = User.findUser(64);
		RtsJdbcHelper helper = new RtsJdbcHelper();
		List<RtsEliminationMatch> list = helper.getPlayerUpcomingMatches(user1);
		
		System.out.println(list);
		System.out.println(list.size());
		
		
	}
	/*
	@Test
	public void testBattleUpdate(){
		User user1  = User.findUser(64);
		
		TypedQuery<EventForce> query = entityManager.createQuery("SELECT force FROM EventForce force " +
				"WHERE force.user = :user", EventForce.class);
		query.setParameter("user", user1);
		List<EventForce> resultList = query.getResultList();
		
		System.out.println(resultList);
		
	}*/
	
	//@Test
	public void addNewSubEvent(){

		RtsEliminationMatch submatch = new RtsEliminationMatch();
		//RtsElimination match = RtsElimination.findRtsElimination(1656);
		
		submatch.setName("WE水友赛#电信赛区 16强赛");
		submatch.setRequiredWin(2);
				
		RtsSingleUserEventForce homeForce = 
			RtsSingleUserEventForce.findRtsSingleUserEventForce(652L);
		
		RtsSingleUserEventForce awayForce = 
			RtsSingleUserEventForce.findRtsSingleUserEventForce(643L);
		
		submatch.setHome(homeForce);
		submatch.setAway(awayForce);
		submatch.setEventForceType(RtsEventForceType.USER);
		submatch.setGameId(2);
		submatch.setRound(16);
		//match.setMap(GameMap.findGameMap(90L));
		submatch.status =  RtsEliminationMatch.Status.OPEN;
		submatch.playGround = RtsLocation.newVs();
		submatch.setParentId(1656);
		submatch.setExpectedTime("2011-8-28 19:30");
		submatch.persist();
		
	}
	
	//@Test
	public void testBattleUpdate(){
		RtsElimination match = RtsElimination.findRtsElimination(1809);
		User user1  = User.findArenaUserByName("北京_鸡佬屎");
		match.playerRegister(user1, null);
		
		match.merge();
		
		/*User user1  = User.findArenaUserByName("RN_老粉");
		if (user1 == null){
			throw new RuntimeException("");
		}
		RtsSingleUserEventForce force = new RtsSingleUserEventForce(user1);
		force.persist();
		match.registeredForces.add(force);
		*/
		
		/*User user1  = User.findUser(4088);
		User user2  = User.findUser(4086);
		User user3  = User.findUser(4094);
		User user4  = User.findUser(4092);
		User user5  = User.findUser(4112);
		User user6  = User.findUser(4113);
		User user7  = User.findUser(4114);
		User user8  = User.findUser(4115);
		match.playerRegister(user1, null);
		match.playerRegister(user2, null);
		match.playerRegister(user3, null);
		match.playerRegister(user4, null);
		match.playerRegister(user5, null);

		match.playerRegister(user6, null);
		match.playerRegister(user7, null);
		match.playerRegister(user8, null);
		match.merge();*/
		
		
	}

	//@Test
	public void testAwardsUpdateFirstTime(){
		
		RtsEliminationMatch match = new RtsEliminationMatch();
		
		match.setName("bb");
		match.setRequiredWin(1);
		
		User user1  = User.findUser(64);
		User user2  = User.findUser(67);
		
		RtsSingleUserEventForce homeForce = new RtsSingleUserEventForce(user1);
		user1.merge();
		homeForce.persist();
		RtsSingleUserEventForce awayForce = new RtsSingleUserEventForce(user2);
		awayForce.persist();
		match.setRound(2);
		match.setHome(homeForce);
		match.setAway(awayForce);
		match.setEventForceType(RtsEventForceType.USER);
		match.setGameId(2);
		match.setMap(GameMap.findGameMap(90L));
		match.status =  RtsEliminationMatch.Status.OPEN;
		match.playGround = RtsLocation.newHf();
		match.persist();
		
		//Add A dummy battle
		match.playerSubmitResult(user1, null, 0, 0,null);
		match.merge();
		
		
	}
	
	//@Test
	public void testAwardsUpdateAfterModification(){
		
		RtsEliminationMatch match = RtsEliminationMatch.findRtsEliminationMatch(1970);
		User user1  = User.findUser(64);
		match.playerSubmitResult(user1, null, 0, 1,null);
		match.merge();
	
	}
	

	//@Test
	public void testTeamFetchByUserGame(){
		
		User user1  = User.findUser(64);
		RtsTeam team = RtsTeam.findRtsTeamByUserGame(user1, 2);
		Assert.assertNotNull(team);
		
	
	}
	
	//@Test
	public void testTeamFetchById(){
		
		RtsTeam team = RtsTeam.findRtsTeam("101eb460-5ef1-4a00-b775-73b8edb33d10");
		Assert.assertNotNull(team);
		
	
	}
	
	@Test
	public void test(){
		
		RtsElimination match = RtsElimination.findRtsElimination(2466);
		User user1  = User.findUser(64);
		RtsTeam team = RtsTeam.findRtsTeamByUserGame(user1, 2);
		match.administratorUnregister(user1, user1, team);
		
	
	}
	/*
	public void testGetUserInfo(){
		
		RtsElimination match = RtsElimination.findRtsElimination(2071);
		for(RtsEventForce force0:match.registeredForces){
			RtsSingleUserEventForce force = (RtsSingleUserEventForce)force0;
			System.out.println(force.getUser().getName() + "\b" + force.get)
		}
		
	
	}*/
}

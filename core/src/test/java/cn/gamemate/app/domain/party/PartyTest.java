package cn.gamemate.app.domain.party;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.clientmsg.DummyMessageService;
import cn.gamemate.app.clientmsg.MessageService;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;

@Configuration
class PartyTestConfig{
	public @Bean UserRepository userRepository(){
		UserRepository users = new UserRepository();
		users.login(new User(1,"user1", "http://"));
		users.login(new User(2,"user2", "http://"));
		users.login(new User(3,"user3", "http://"));
		users.login(new User(4,"user4", "http://"));
		users.login(new User(5,"user5", "http://"));
		users.login(new User(6,"user6", "http://"));
		users.login(new User(7,"user7", "http://"));
		users.login(new User(8,"user8", "http://"));
		users.login(new User(9,"user9", "http://"));
		users.login(new User(10,"user10", "http://"));
		return users;
	}
	
	public @Bean PartyManager partyManager(){
		return new PartyManager();
	}
	public @Bean org.safehaus.uuid.UUIDGenerator uuidGenerator (){
		return org.safehaus.uuid.UUIDGenerator.getInstance();
	}
	public @Bean MessageService messageService(){
		DummyMessageService messageService = new DummyMessageService();
		return messageService;
	}
}


public class PartyTest {
	
	private ApplicationContext ctx;
	private UserRepository users;
	private PartyManager partyManager;
	User user1, user2, user3, user4;
	
	@Before
	public void setUp(){
		ctx = new AnnotationConfigApplicationContext(PartyTestConfig.class);
		users = ctx.getBean("userRepository", UserRepository.class);
		partyManager = ctx.getBean(PartyManager.class);
		user1=users.getUser(1);
		user2=users.getUser(2);
		user3=users.getUser(3);
		user4=users.getUser(4);
	}
	
	@Test
	public void createParty(){
		
		ArrayList<User> arrayList = new ArrayList<User>();
		arrayList.add(users.getUser(2));
		arrayList.add(users.getUser(3));
		DefaultParty party = partyManager.userCreateParty(users.getUser(1), arrayList);
		//TODO: how to inject this in test?
		party.partyManager = partyManager;
		Assert.assertEquals(3, party.members.size());
		Assert.assertSame(partyManager.getParty(users.getUser(1), false), party);
		Assert.assertEquals(Integer.valueOf(1), party.getLeaderId());
		Assert.assertTrue(user1.isInParty());
		Assert.assertTrue(user2.isInParty());
		Assert.assertTrue(user3.isInParty());
		
		
	}
	
	@Test
	public void inviteUser(){
		
		createParty();
		User leader = users.getUser(1);
		DefaultParty party = partyManager.getParty(leader);
		party.userInviteUser(leader, users.getUser(4));
		Assert.assertEquals(4, party.members.size());
		Assert.assertTrue(user4.isInParty());
	}
	
	@Test
	public void partyMemberLeave(){
		createParty();
		User leader = users.getUser(1);
		DefaultParty party = partyManager.getParty(leader);
		
		//user2 leave the team, 2 left.
		party.userLeave(users.getUser(2));
		Assert.assertEquals(2, party.members.size());
		Assert.assertFalse(user2.isInParty());

		//user3 leave the team, the team is closed.
		party.userLeave(users.getUser(3));
		Assert.assertEquals(0, party.members.size());
		Assert.assertNull(partyManager.getParty(leader, false));
		Assert.assertFalse(user1.isInParty());
		Assert.assertFalse(user3.isInParty());
	}
	
	@Test
	public void partyLeaderLeave(){
		createParty();
		User leader = users.getUser(1);
		DefaultParty party = partyManager.getParty(leader);
		
		//user1 leave the team, leader changed to user2.
		party.userLeave(users.getUser(1));
		Assert.assertEquals(2, party.members.size());
		Assert.assertEquals(Integer.valueOf(2), party.getLeaderId());
		Assert.assertFalse(user1.isInParty());
		
		//user2 leave the team, the team is closed.
		party.userLeave(users.getUser(2));
		Assert.assertEquals(0, party.members.size());
		Assert.assertFalse(user2.isInParty());
		Assert.assertFalse(user3.isInParty());
	}
//	
//	@Test
//	public void testUserDrop(){
//		createParty();
//		DefaultParty party = partyManager.getParty(user1);
//		
//		users.drop(user1);
//		Assert.assertEquals(2, party.members.size());
//		Assert.assertEquals(Integer.valueOf(2), party.getLeaderId());
//		Assert.assertFalse(user1.isInParty());
//		
//		
//	}
	
	
	
	

}

package cn.gamemate.app.domain.arena;

import java.util.List;

import junit.framework.Assert;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.junit.Test;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import proto.res.ResArena;
import cn.gamemate.app.cassandra.Cassandra;

import com.google.protobuf.InvalidProtocolBufferException;


@Configuration
class BattleTestConfiguration {
	
	//TODO: move this to other configuration
	private String CASSANDRA_ADDRESS = "172.16.4.2:9160";
	private String CASSANDRA_CLUSTER = "Test Cluster";
	private String CASSANDRA_KEYSPACE = "gm";

	@Bean  UUIDGenerator uuidGenerator() {
		return UUIDGenerator.getInstance();

	}
	
	@DependsOn("aspect")
	@Bean CassandraHostConfigurator cassandraConfigurator(){
		return new CassandraHostConfigurator(CASSANDRA_ADDRESS);
	}
	
	@DependsOn("aspect")
	@Bean ThriftCluster cassandraCluster(){
		return new ThriftCluster(CASSANDRA_CLUSTER, cassandraConfigurator());
	}
	
	@DependsOn("aspect")
	@Bean Keyspace gmKeyspace(){
		return HFactory.createKeyspace(CASSANDRA_KEYSPACE, cassandraCluster());
	}
	
	@DependsOn("aspect")
	@Bean Cassandra cassandra(){
		Cassandra cassandra = new Cassandra();
		cassandra.setKeyspace(gmKeyspace());
		return cassandra;
	}
	
	@Bean org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect aspect(){
		return org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect.aspectOf();
	}
	
	/**
	 * @return ArenaSnapshot in which user80 and user89 play dota 1v1. 
	 * @throws InvalidProtocolBufferException
	 */
	@Bean ResArena.Arena arenaSnapshot() throws InvalidProtocolBufferException{
		byte[] encoded = new byte[] { 8, -110, -5, -94, -124, 4, 18, 8, 8, 2,
				18, 4, 100, 111, 116, 97, 26, 79, 8, 2, 18, 10, 87, 97, 114,
				67, 114, 97, 102, 116, 32, 51, 34, 63, 10, 11, 49, 46, 50, 52,
				46, 49, 46, 54, 51, 55, 52, 18, 4, 49, 46, 50, 52, 26, 8, 119,
				97, 114, 51, 46, 101, 120, 101, 34, 32, 57, 53, 57, 55, 50, 49,
				97, 48, 54, 51, 51, 56, 97, 101, 52, 99, 57, 101, 56, 49, 98,
				97, 57, 52, 100, 50, 99, 49, 98, 54, 98, 100, 34, -102, 1, 8,
				83, 18, 19, 68, 111, 116, 65, 32, 65, 108, 108, 115, 116, 97,
				114, 115, 32, 54, 46, 55, 49, 98, 32, 2, 50, 32, 98, 48, 53,
				54, 54, 55, 55, 49, 49, 57, 57, 49, 50, 57, 51, 98, 57, 55, 51,
				48, 101, 54, 99, 57, 49, 51, 102, 55, 53, 53, 55, 101, 58, 41,
				109, 97, 112, 47, 87, 97, 114, 67, 114, 97, 102, 116, 51, 47,
				68, 111, 116, 65, 32, 65, 108, 108, 115, 116, 97, 114, 115, 32,
				118, 54, 46, 55, 49, 98, 32, 67, 78, 46, 119, 51, 120, 64, -56,
				-62, -39, 3, 74, 45, 109, 97, 112, 47, 87, 97, 114, 67, 114,
				97, 102, 116, 51, 47, 68, 111, 116, 65, 32, 65, 108, 108, 115,
				116, 97, 114, 115, 32, 118, 54, 46, 55, 49, 98, 32, 67, 78, 46,
				119, 51, 120, 46, 112, 110, 103, 42, 6, 71, 65, 77, 73, 78, 71,
				50, 83, 10, 73, 8, 80, 18, 2, 99, 54, 34, 6, 111, 110, 108,
				105, 110, 101, 74, 44, 117, 115, 101, 114, 47, 56, 48, 47, 98,
				56, 102, 99, 56, 98, 52, 48, 101, 51, 101, 98, 99, 56, 98, 100,
				57, 100, 50, 100, 53, 102, 49, 100, 97, 48, 101, 55, 51, 97,
				50, 51, 46, 112, 110, 103, 96, 0, 112, 0, 120, 1, -120, 1,
				-110, -5, -94, -124, 4, 16, 0, 24, 0, 40, 1, 48, 0, 50, 8, 16,
				1, 24, 0, 40, 1, 48, 0, 50, 8, 16, 2, 24, 0, 40, 1, 48, 0, 50,
				8, 16, 3, 24, 0, 40, 1, 48, 0, 50, 8, 16, 4, 24, 0, 40, 1, 48,
				0, 50, 64, 10, 54, 8, 89, 18, 2, 99, 57, 34, 6, 111, 110, 108,
				105, 110, 101, 74, 25, 117, 115, 101, 114, 47, 100, 101, 102,
				97, 117, 108, 116, 95, 112, 111, 114, 116, 114, 97, 105, 116,
				46, 106, 112, 103, 96, 0, 112, 0, 120, 1, -120, 1, -110, -5,
				-94, -124, 4, 16, 5, 24, 1, 40, 1, 48, 1, 50, 8, 16, 6, 24, 1,
				40, 1, 48, 0, 50, 8, 16, 7, 24, 1, 40, 1, 48, 0, 50, 8, 16, 8,
				24, 1, 40, 1, 48, 0, 50, 8, 16, 9, 24, 1, 40, 1, 48, 0, 58, 14,
				10, 8, 100, 111, 116, 97, 109, 111, 100, 101, 18, 2, 114, 100,
				58, 12, 10, 6, 104, 111, 115, 116, 73, 68, 18, 2, 56, 48, 58,
				12, 10, 6, 108, 101, 97, 100, 101, 114, 18, 2, 56, 48, 66, 8,
				53, 53, 57, 48, 55, 56, 54, 49, 74, 10, 8, 0, 18, 6, 84, 101,
				97, 109, 32, 49, 74, 10, 8, 1, 18, 6, 84, 101, 97, 109, 32, 50,
				88, 0, 114, 2, 114, 100, 122, 36, 98, 53, 102, 54, 52, 100,
				101, 101, 45, 100, 97, 56, 55, 45, 52, 51, 55, 49, 45, 56, 49,
				49, 49, 45, 55, 98, 50, 53, 50, 53, 97, 53, 101, 101, 99, 50,
				-112, 1, 80, -94, 1, 6, 110, 111, 114, 109, 97, 108, -86, 1,
				14, 68, 111, 116, 65, 32, -24, -121, -86, -25, -108, -79, -24,
				-75, -101, -78, 1, 1, 50 };
		return ResArena.Arena.newBuilder().mergeFrom(encoded).build();
	}
	
	
}

public class BattleTest {
	private ApplicationContext ctx = 
		new AnnotationConfigApplicationContext(BattleTestConfiguration.class);
	
	@Test
	public void createAndRetriveBattleTest(){
		ResArena.Arena arenaSnapshot = ctx.getBean("arenaSnapshot", ResArena.Arena.class);
		Battle battle = Battle.createAndSave(arenaSnapshot);
		UUID uuid = battle.getUuid();
		
		Battle retrievedBattle = Battle.get(uuid, Battle.class);
		assertBattle(battle, retrievedBattle);
	}
	
	@Test
	public void getUserRecentBattlesTest(){
		ResArena.Arena arenaSnapshot = ctx.getBean("arenaSnapshot", ResArena.Arena.class);
		System.out.println(arenaSnapshot);
		Battle battle1 = Battle.createAndSave(arenaSnapshot);
		Battle battle2 = Battle.createAndSave(arenaSnapshot);
		Battle battle3 = Battle.createAndSave(arenaSnapshot);
		Battle battle4 = Battle.createAndSave(arenaSnapshot);
		Battle battle5 = Battle.createAndSave(arenaSnapshot);
		
		List<Battle> battlesOfUser80 = Battle.getUserRecentBattles(80, 2, 0, 5);
		Assert.assertEquals(5, battlesOfUser80.size());
		//in last-first order
		assertBattle(battle5, battlesOfUser80.get(0));
		assertBattle(battle4, battlesOfUser80.get(1));
		assertBattle(battle3, battlesOfUser80.get(2));
		assertBattle(battle2, battlesOfUser80.get(3));
		assertBattle(battle1, battlesOfUser80.get(4));
		
	}
	
	public void assertBattle(Battle expected, Battle actual){
		Assert.assertEquals(expected.getUuid(), actual.getUuid());
		Assert.assertEquals(expected.getStartTime(), actual.getStartTime());
		
	}
	
	
	
	
	
	
	
}


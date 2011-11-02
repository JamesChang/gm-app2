package cn.gamemate.app;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.joda.time.DateTime;
import org.safehaus.uuid.UUIDGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.gamemate.app.cassandra.Cassandra;
import cn.gamemate.app.domain.event.EntityEvent;

import com.rabbitmq.client.ShutdownSignalException;




@Configuration
class mainConfiguration {
	
	
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
	
	
}



public class JustRun {
	private final static String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws IOException, ShutdownSignalException, InterruptedException, ParseException {
		
		/*DateTime dateTime = new DateTime("2011-8-18T10:23");
		Date date = dateTime.toDate();
		*/
//		ApplicationContext context;
//		CassandraDotaBattleDao dao;
//		DefaultDotaRepProcessingService service ;
//		
//		final String t = "e107c208-7baf-11e0-ab86-db173d49233e";
//		final int u = 279;
//		
//		context = new  AnnotationConfigApplicationContext(cn.gamemate.app.domain.event.dota.DotaRepInfoTestConfiguration.class);
//		dao = new CassandraDotaBattleDao(context.getBean("gmKeyspace", Keyspace.class));
//		String data = dao.getReplayInfoAsString(t, u);
//		
//		
//		
//
//		try {
//			FileWriter writer = new FileWriter("/home/jameszhang/w/app-parent/core/src/test/resources/dota_cdp_output/"
//					+ t);
//			writer.write(data);
//			writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		
//		ConnectionFactory connectionFactory = new ConnectionFactory();
//		connectionFactory.setHost("dev.gamemate.cn");
//		Connection conn = connectionFactory.newConnection();
//		
//		Channel channel = conn.createChannel();
//		
//		String queueName = "deferred_tasks";
////
//		channel.exchangeDeclare("taskqueue", "direct", true);
//		//queueName = "TQ_" + queueName;
//		channel.queueDeclare(queueName, true, false, false, null);
//
//		channel.queueBind(queueName, "taskqueue", queueName);
//		
//		
//		
//		QueueingConsumer consumer = new QueueingConsumer(channel);
//		channel.basicConsume(queueName, consumer);
//		while(true){
//			Delivery delivery = consumer.nextDelivery();
//			System.out.println(delivery);
//		}
		
		
		
//		long l = System.currentTimeMillis();
//		String s = new SimpleDateFormat().getDateTimeInstance().format(l);
//		System.out.println(s);
//		
		
//		String data = "{\"a\":{\"a1\":{\"a11\":\"hello\"}}, \"errors\":null,\"header\":{\"intro\":\"Warcraft III recorded game\\u001a\"}}";
//		
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode node=null;
//		try {
//			node = mapper.readValue(data, JsonNode.class);
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		JsonNode aNode = node.get("a");
//		JsonNode a1Node = aNode.get("a1");
//		JsonNode a11Node = a1Node.get("a11");
//		JsonNode errorsNode = node.get("errors");
//		JsonNode headerNode = node.get("header");
//		JsonNode introNode = headerNode.get("intro");
//		
////		
////		
//////		
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"META-INF/spring/applicationContext.xml");
		EntityEvent event = EntityEvent.findEntityEvent(3040);
		System.out.println("xxx");
//		Battle battle = Battle.get(new UUID("2a78a4e3-5773-11e0-ad86-01d49cf43f74"),
//			DotaBattle.class);
//		DotaBattle dotaBattle = (DotaBattle) battle;
//		DotaRepInfo repInfo = dotaBattle.getReplayInfo(300);
//		System.out.println(repInfo.getCdp_output());
		//System.out.println(repInfo.getParsedReplyInfo().get("header").get("intro"));
		
		
//		
//		
//		
//		MultigetSliceQuery<UUID,String,byte[]> battleQuery = HFactory.createMultigetSliceQuery(
//				context.getBean(Keyspace.class), 
//				JugUuidSerializer.get(), 
//				StringSerializer.get(), 
//				BytesArraySerializer.get());
//		battleQuery.setColumnFamily("Battles");		
//		List<UUID> battleKeys = new ArrayList<UUID>();
//		battleKeys.add(new UUID("64252189-55c8-11e0-b264-99b33082a2a7"));
//		battleKeys.add(new UUID("9208dc9a-55e9-11e0-b760-cb8143f1534a"));
//		
//		///??? why cast
//		battleQuery.setKeys(battleKeys);
//		battleQuery.setRange(null, null, false, 20);
//		QueryResult<Rows<UUID, String, byte[]>> _BattleQueryResult = battleQuery.execute();
//		System.out.println(_BattleQueryResult.get().getCount());
//		for (Row<UUID, String, byte[]> row:_BattleQueryResult.get()){
//			System.out.println(row.getKey());
//		}
		
		
		
		//List<Battle> battles = Battle.getUserRecentBattles(80, 2);
		//System.out.println(battles);
		//Battle battle = Battle.get(new UUID("64252189-55c8-11e0-b264-99b33082a2a7"));
		//System.out.println(battle);
		//System.exit(0);
//		
//		
//		BattlePlayerAwards awards = new BattlePlayerAwards();
//		awards.setActivity(1);
//		awards.setPower(0.32);
//		String json = awards.toJson();
//		System.out.println(json);
//		
//		BattlePlayerAwards awards2 = BattlePlayerAwards.fromJson(json);
//		
//		System.out.println(awards2);
//		
//		Map<Integer, BattlePlayerAwards> maps = new  TreeMap<Integer, BattlePlayerAwards>();
//		
//		BattleAwards allAwards = new BattleAwards();
//		maps.put(1, awards);
//		ObjectMapper mapper = new ObjectMapper();
//		
//		String all = mapper.writeValueAsString(maps);
//		System.out.println(all);
////		
//		BattleAwards readValue = mapper.readValue(all, new TypeReference<BattleAwards>(){});
//		System.out.println(readValue);
//		
		
		//ApplicationContext context = new  AnnotationConfigApplicationContext(mainConfiguration.class);
		
//		UUID uuid = new UUID("a046426f-5518-11e0-b264-99b33082a2a7");
//		
//		Battle battle = Battle.findByUuid(uuid);
//		
		
		//System.setProperty("msgsrv.host", "192.168.110.198");
		/*
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
		System.out.println();*/
//		
//		
////		EventCenter eventCenter = ctx.getBean(EventCenter.class);
////		ArenaListEvent05 event = eventCenter.getEvent(2);
//		User user1 = new User();
//		user1.setId(64);
//		user1.setName("user1");
//		user1.setPortrait("");
//		user1.setStatus(UserStatus.ONLINE);
//		User user2 = new User();
//		user2.setId(67);
//		user2.setName("user2");
//		user2.setPortrait("");
//		user2.setStatus(UserStatus.ONLINE);
//		UserRepository users = ctx.getBean(UserRepository.class);
//		users.login(user1);
//		users.login(user2);
//		
//		PartyManager partyManager = ctx.getBean(PartyManager.class);
//		ArrayList<User> arrayList = new ArrayList<User>();
//		arrayList.add(user2);
//		partyManager.userCreateParty(user1, arrayList);
//		
		
	}

}

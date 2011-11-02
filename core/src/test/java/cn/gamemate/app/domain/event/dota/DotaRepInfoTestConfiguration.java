package cn.gamemate.app.domain.event.dota;

import java.util.Arrays;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.safehaus.uuid.UUIDGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import cn.gamemate.app.cassandra.Cassandra;
import cn.gamemate.taskqueue.rabbitmq.RabbitTaskQueueFactory;
import cn.gamemate.taskqueue.rabbitmq.TaskQueueConfiguration;

@Configuration
public
class DotaRepInfoTestConfiguration {
	
	
	private String CASSANDRA_ADDRESS = "dev.gamemate.cn:9160";
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
	/*
	@Bean RabbitTaskQueueFactory  taskQueueFactory(){
		RabbitTaskQueueFactory service = new RabbitTaskQueueFactory("dev.gamemate.cn");

		TaskQueueConfiguration configuration1 = new TaskQueueConfiguration();
		String queueName = "dota_replay_parsed";
		configuration1.setName(queueName); 
		configuration1.setAsyncReceive(true);
		configuration1.setAutoAck(false);
		configuration1.setAmqpName("dota_replay_parsed");
		
		service.setQueues(Arrays.asList(configuration1));
		service.prepareAndBind();
		return service;
	}*/
	
}
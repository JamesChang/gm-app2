package cn.gamemate.app.cassandra;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HectorConfiguration {
	
	@Value("${cassandra.address}")
	private String CASSANDRA_ADDRESS;
	@Value("${cassandra.cluster}")
	private String CASSANDRA_CLUSTER;
	@Value("${cassandra.keyspace}")
	private String CASSANDRA_KEYSPACE;

	@Bean CassandraHostConfigurator cassandraConfigurator(){
		return new CassandraHostConfigurator(CASSANDRA_ADDRESS);
	}
	
	@Bean ThriftCluster cassandraCluster(){
		return new ThriftCluster(CASSANDRA_CLUSTER, cassandraConfigurator());
	}
	
	@Bean Keyspace gmKeyspace(){
		return HFactory.createKeyspace(CASSANDRA_KEYSPACE, cassandraCluster());
	}
	
	@Bean Cassandra cassandra(){
		Cassandra cassandra = new Cassandra();
		cassandra.setKeyspace(gmKeyspace());
		return cassandra;
	}
}

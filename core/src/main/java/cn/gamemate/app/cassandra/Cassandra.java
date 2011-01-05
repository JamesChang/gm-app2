package cn.gamemate.app.cassandra;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

public class Cassandra {
	
	private Keyspace keyspace;
	private final StringSerializer stringSerializer = StringSerializer.get();
	
	public Keyspace getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(Keyspace keyspace) {
		this.keyspace = keyspace;
	}

	

	public StringSerializer getStringSerializer() {
		return stringSerializer;
	}
	
	public String get(final String key, final String columnFamily, final String column){
		ColumnQuery<String, String, String> q = HFactory.createColumnQuery(keyspace, 
				stringSerializer, stringSerializer, stringSerializer);
		QueryResult<HColumn<String, String>> r =q
			.setKey(key)
			.setColumnFamily(columnFamily)
			.setName(column)
			.execute();
		HColumn<String, String> c = r.get();
		return c!=null ? c.getValue():null;
	}
	
	public Mutator<String> newMutator(){
		return HFactory.createMutator(keyspace, stringSerializer);
	}

	

}

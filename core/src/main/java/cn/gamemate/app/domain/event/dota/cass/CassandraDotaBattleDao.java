package cn.gamemate.app.domain.event.dota.cass;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.ColumnQuery;

import org.safehaus.uuid.UUID;

import cn.gamemate.app.cassandra.JugUuidSerializer;
import cn.gamemate.app.domain.arena.Battle;
import cn.gamemate.app.domain.event.dota.DotaBattleDao;
import cn.gamemate.app.domain.event.dota.DotaRepInfo;

public class CassandraDotaBattleDao implements DotaBattleDao{
	
	public final String parsedResultPrefix = "PR_";
	
	protected final Keyspace keyspace;
	
	public CassandraDotaBattleDao(Keyspace keyspace) {
		this.keyspace = keyspace;
	}
	
	@Override
	public DotaRepInfo getReplayInfo(String battleId, Integer submitterId) {
		
		String data = getReplayInfoAsString(battleId, submitterId);
		
		DotaRepInfo repInfo = DotaRepInfo.fromJson(data);
		
		return repInfo;
	}
	
	public String getReplayInfoAsString(String battleId, Integer submitterId) {
		ColumnQuery<UUID,String,String> query = HFactory.createColumnQuery(keyspace,
				JugUuidSerializer.get(),
				StringSerializer.get(),
				StringSerializer.get());
		query.setKey(new UUID(battleId))
			.setColumnFamily(Battle.BattleTable)
			.setName("PR_"+submitterId.toString());
		HColumn<String, String> column = query.execute().get();
		String data = column.getValue();
		return data;
	}
	

}

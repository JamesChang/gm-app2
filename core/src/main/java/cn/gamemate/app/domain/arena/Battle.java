package cn.gamemate.app.domain.arena;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.joda.time.DateTime;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.res.ResArena;
import cn.gamemate.app.cassandra.JugUuidSerializer;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.ObjectNotFound;

import com.google.common.base.Objects;
import com.google.protobuf.GeneratedMessage.Builder;
import com.google.protobuf.InvalidProtocolBufferException;

@Configurable
public class Battle implements DomainModel{
	
	public static final long serializationVersion= 1L; 
	
	public enum BattleStatus {NEW, PENDING, DONE};
	
	protected static final Logger logger= LoggerFactory.getLogger(Battle.class);
	
	@Autowired
	protected UUIDGenerator uuidGenerator;
	
	@Autowired
	protected Keyspace gmKeyspace;
	//required
	protected UUID uuid;
	
	protected String text;	
	protected BattleAwards awards;
	protected proto.res.ResArena.Arena arenaSnapshot;
	//required
	protected BattleStatus status;
	//required
	protected Long eventId;
	protected DateTime startTime;
	//protected JSON data;
	
	public DateTime getStartTime() {
		return startTime;
	}

	protected static final String BattleTable =  "Battles";
	
	
	public UUID getUuid() {
		return uuid;
	}
	
	protected static UUIDGenerator getUuidGenerator(){
		
		UUIDGenerator generator = new Battle().uuidGenerator;
		if (generator == null) throw new IllegalStateException("UUIDGenerator has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return generator;
	}
	protected static Keyspace keyspace(){
		
		Keyspace gmKeyspace = new Battle().gmKeyspace;
		if (gmKeyspace == null) throw new IllegalStateException("gmKeyspace has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return gmKeyspace;
	}
	
	//TODO: this is OCP violation
	protected static String getCassandraUserIndexName(Integer gameid){
		if (gameid.equals(1)){
			return "UserWar3Battles";
		}else if (gameid.equals(2)){
			return "UserDotaBattles";
		}else if (gameid.equals(3)){
			return "UserScBattles";
		}
		throw new DomainModelRuntimeException("no gameid="+gameid);
		
	}


	public static Battle createAndSave(proto.res.ResArena.Arena arenaSnapshot){
		Battle battle = new Battle();
		battle.status = BattleStatus.NEW;
		battle.arenaSnapshot = arenaSnapshot;
		battle.uuid = getUuidGenerator().generateTimeBasedUUID();
		battle.startTime = new DateTime();
		
		//insert into BattleTable
		HFactory.createMutator(keyspace(), JugUuidSerializer.get())
			.addInsertion(battle.uuid, BattleTable, 
					HFactory.createStringColumn(
							"status", battle.status.toString()))
			.addInsertion(battle.uuid, BattleTable, 
					HFactory.createColumn(
							"arenaSnapshot", battle.arenaSnapshot.toByteArray(),
							StringSerializer.get(), BytesArraySerializer.get()))
			.addInsertion(battle.uuid, BattleTable, 
					HFactory.createColumn(
							"event_id", Long.parseLong(battle.arenaSnapshot.getEventId()),
							StringSerializer.get(), LongSerializer.get()))		
			.addInsertion(battle.uuid, BattleTable, 
					HFactory.createColumn(
							"t_start", battle.startTime.getMillis(),
							StringSerializer.get(), LongSerializer.get()))	
			.execute();
		
		//create index (User, Game) --> Battle 
		Mutator<String> mutator = HFactory.createMutator(keyspace(), StringSerializer.get());
		String indexTableName = getCassandraUserIndexName(arenaSnapshot.getLogicalGame().getId()); 
		for(proto.res.ResArena.ArenaSlot slot:arenaSnapshot.getPlayersList()){
			if (slot.hasUser()){
				mutator.addInsertion(
							Integer.toString(slot.getUser().getId()), 
							indexTableName,
							HFactory.createColumn(
									battle.uuid.asByteArray(),
									"{\"game\":"+arenaSnapshot.getEventName() + "}",
									BytesArraySerializer.get(), StringSerializer.get()));
					
			}
		}
		mutator.execute();
		return battle;
	}
	
	public static Battle get(UUID uuid) {
		//TODO: can this query be cached?
		SliceQuery<UUID, String, byte[]> query = HFactory.createSliceQuery(keyspace(), JugUuidSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
		ColumnSlice<String, byte[]> result = query.setKey(uuid).setColumnFamily(BattleTable).setRange("", "", false, 20).execute().get();		
		Battle battle = fromColumnSlice(uuid, result);
		return battle;
	}
	
	protected static Battle fromColumnSlice(UUID uuid,ColumnSlice<String, byte[]> columnSlice){
		Battle battle = new Battle();
		battle.uuid = uuid;
		
		HColumn<String, byte[]> battleStatusColumn = columnSlice.getColumnByName("status");
		if (battleStatusColumn == null){
			throw new ObjectNotFound(Battle.class, uuid);
		}
		
		byte[] value = battleStatusColumn.getValue();
		String statusString = new String(value);
		battle.status  = BattleStatus.valueOf(statusString);
		
		HColumn<String, byte[]> arenaSnapshotColumn = columnSlice.getColumnByName("arenaSnapshot");
		if (arenaSnapshotColumn == null){
			throw new ObjectNotFound(Battle.class, uuid);
		}
		try {
			battle.arenaSnapshot = ResArena.Arena.newBuilder().mergeFrom((arenaSnapshotColumn.getValue())).build();
		} catch (InvalidProtocolBufferException e1) {
			//TODO: throw DataAccess Exception 
			throw new RuntimeException(e1);
		}
		
		HColumn<String, byte[]> eventIdColumn = columnSlice.getColumnByName("event_id");
		if (eventIdColumn == null){
			//TODO: throw DataAccess Exception 
			throw new ObjectNotFound(Battle.class, uuid);
		}
		battle.eventId = LongSerializer.get().fromBytes(eventIdColumn.getValue());
		

		HColumn<String, byte[]> startTimeColumn = columnSlice.getColumnByName("t_start");
		if (startTimeColumn == null){
			//TODO: throw DataAccess Exception 
			throw new ObjectNotFound(Battle.class, uuid);
		}
		battle.startTime = new DateTime(LongSerializer.get().fromBytes(startTimeColumn.getValue()));
		
		HColumn<String, byte[]> textColumn = columnSlice.getColumnByName("text");
		if (textColumn != null){
			battle.text = StringSerializer.get().fromBytes(textColumn.getValue());
		}else{
			battle.text = "UNKNOWN";
		}
		
		HColumn<String, byte[]> awardsColumns = columnSlice.getColumnByName("awards");
		if (textColumn != null){
			try{
				battle.awards = BattleAwards.fromJson(StringSerializer.get().fromBytes(awardsColumns.getValue()));
			}catch(Exception e){
				//TODO: throw DataAccess Exception 
				throw new RuntimeException(e);
			}
		}else{
			battle.awards = new BattleAwards();
		}
		
		return battle;
	}

	@Override
	public Builder toProtobuf() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static List<Battle> getUserRecentBattles(Integer userid, Integer gameid, 
			Integer offset, Integer limit){
		List<Battle> result = new ArrayList<Battle>();
		//query user-game-battle index
		String indexName = getCassandraUserIndexName(gameid);
		SliceQuery<String,UUID,byte[]> indexQuery = HFactory.createSliceQuery(keyspace(), StringSerializer.get(), 
				JugUuidSerializer.get(), BytesArraySerializer.get());
		QueryResult<ColumnSlice<UUID, byte[]>> _indexQueryResult = indexQuery.setKey(userid.toString())
			 .setColumnFamily(indexName)
			 .setRange(null, null, true, limit)
			 .execute();
		ColumnSlice<UUID, byte[]> indexQueryResult = _indexQueryResult.get();
		
		//query battle
		MultigetSliceQuery<UUID,String,byte[]> battleQuery = HFactory.createMultigetSliceQuery(keyspace(), 
				JugUuidSerializer.get(), 
				StringSerializer.get(), 
				BytesArraySerializer.get());
		battleQuery.setColumnFamily(BattleTable);		
		List<UUID> battleKeys = new ArrayList<UUID>();
		for (HColumn<UUID, byte[]>  column:indexQueryResult.getColumns()){
			battleKeys.add(column.getName());
		}
		///??? why cast
		battleQuery.setKeys(battleKeys);
		battleQuery.setRange(null, null, false, 20);		
		QueryResult<Rows<UUID, String, byte[]>> _BattleQueryResult = battleQuery.execute();
		Rows<UUID, String, byte[]> rows = _BattleQueryResult.get();
		for(UUID tUUID:battleKeys){
			Row<UUID, String, byte[]> row = rows.getByKey(tUUID);
			if (row == null) continue;
			result.add(fromColumnSlice(row.getKey(), row.getColumnSlice()));
		}
		return result;
		
	}
	
	public static List<Battle> getUserRecentBattles(Integer userid, Integer gameid){
		return getUserRecentBattles(userid, gameid, 0, 20);
	}
	public List<Battle> getEventRecentBattles(){
		return null;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(Battle.class)
		.addValue(uuid)
		.addValue(status)
		.addValue(text)
		.addValue(startTime)
		.toString();
	}
	

}

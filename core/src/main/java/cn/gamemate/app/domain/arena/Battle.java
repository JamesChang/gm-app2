package cn.gamemate.app.domain.arena;

import java.util.ArrayList;
import java.util.Collections;
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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
import cn.gamemate.app.domain.event.awards.BattleAwards;

import com.google.common.base.Objects;
import com.google.protobuf.GeneratedMessage.Builder;
import com.google.protobuf.InvalidProtocolBufferException;

@Configurable
public class Battle implements DomainModel{
	
	public static final long serializationVersion= 1L; 
	public static final String INVALID = "INVALID";
	public static final String FINISHED = "FINISHED";
	public static final String UNKNOWN = "UNKNOWN";
	public static final String QUIT = "QUIT";
	
	
	public enum BattleStatus {NEW, PENDING, DONE};
	
	protected static final Logger logger= LoggerFactory.getLogger(Battle.class);
	
	@Autowired
	protected UUIDGenerator uuidGenerator;
	
	@Autowired
	protected Keyspace gmKeyspace;
	//required
	protected UUID uuid;
	
	protected String text;	
	private boolean textModified = false;
	
	protected BattleAwards user_game_awards;
	private boolean  userGameAwardsModified = false;
	
	
	public BattleAwards getUserGameAwards() {
		return user_game_awards;
	}

	public void setUserGameAwards(BattleAwards awards) {
		this.user_game_awards = awards;
		this.userGameAwardsModified = true;
	}

	

	protected BattleAwards user_awards;
	private boolean  userAwardsModified = false;
	
	
	public BattleAwards getUserAwards() {
		return user_awards;
	}

	public void setUserAwards(BattleAwards awards) {
		this.user_awards = awards;
		this.userAwardsModified = true;
	}
	

	protected BattleAwards user_event_awards;
	private boolean  userEventAwardsModified = false;
	
	
	public BattleAwards getUserEventAwards() {
		return user_event_awards;
	}

	public void setUserEventAwards(BattleAwards awards) {
		this.user_event_awards = awards;
		this.userEventAwardsModified = true;
	}
	
	
	protected proto.res.ResArena.Arena arenaSnapshot;
	public proto.res.ResArena.Arena getArenaSnapshot() {
		return arenaSnapshot;
	}
	
	public int getLogicalGameId(){
		return arenaSnapshot.getLogicalGame().getId();
	}

	//required
	protected BattleStatus status;
	private boolean statusModified = false;
	
	//required
	protected Long eventId;
	protected DateTime startTime;

	private List<Float> outcome;
	private boolean outcomeModified = false;

	public BattleStatus getStatus(){
		return status;
	}
	
	public void setStatus(BattleStatus status){
		this.status = status;
		statusModified = true;
	}
	
	public List<Float> getOutcome(){
		return outcome; 
	}
	
	public void setOutcome(List<Float> outcome){
		this.outcome = new ArrayList<Float>(outcome);
		this.outcomeModified = true;
	}
	
	
	
	//protected JSON data;
	
	public DateTime getStartTime() {
		return startTime;
	}

	public static final String BattleTable =  "Battles";
	public static final String EventBattlesIndexTable = "EventBattles";
	
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
		
		//create index Event --> Battle
		for(proto.res.ResArena.ArenaSlot slot:arenaSnapshot.getPlayersList()){
			if (slot.hasUser()){
				mutator.addInsertion(
							arenaSnapshot.getEventId(), 
							EventBattlesIndexTable,
							HFactory.createColumn(
									battle.uuid.asByteArray(),
									" ",
									BytesArraySerializer.get(), StringSerializer.get()));
					
			}
		}
		mutator.execute();
		
		return battle;
	}
	
	
	public static <T extends Battle> T get(UUID uuid, Class<T> cls) {
		//TODO: can this query be cached?
		SliceQuery<UUID, String, byte[]> query = HFactory.createSliceQuery(keyspace(), JugUuidSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
		ColumnSlice<String, byte[]> result = query.setKey(uuid).setColumnFamily(BattleTable).setRange("", "", false, 20).execute().get();		
		T battle = T.fromColumnSlice(uuid, result, cls);
		return battle;
	}
	
	protected static <T extends Battle> T fromColumnSlice(
			UUID uuid,
			ColumnSlice<String, byte[]> columnSlice,
			Class<T> cls){
		T battle;
		try {
			battle = cls.newInstance();
		} catch (InstantiationException e2) {
			throw new RuntimeException(e2);
		} catch (IllegalAccessException e2) {
			throw new RuntimeException(e2);
		}
		battle.uuid = uuid;
		
		//TODO: if column count == 0, throw new ObjectNotFound
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
		
		HColumn<String, byte[]> awardsColumns = columnSlice.getColumnByName("user_game_awards");
		if (awardsColumns != null){
			try{
				battle.user_game_awards = BattleAwards.fromJson(StringSerializer.get().fromBytes(awardsColumns.getValue()));
			}catch(Exception e){
				//TODO: throw DataAccess Exception 
				throw new RuntimeException(e);
			}
		}else{
			battle.user_game_awards = new BattleAwards();
		}
		
		HColumn<String, byte[]> outcomeColumns = columnSlice.getColumnByName("outcome");
		if (outcomeColumns != null){
				String data = StringSerializer.get().fromBytes(outcomeColumns.getValue());
				ObjectMapper mapper = new ObjectMapper();
				List<Float> value2;
				try {
					value2 = mapper.readValue(data, new TypeReference<List<Float>>(){});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				battle.outcome = value2;
		}else{
			battle.outcome = Collections.emptyList();
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
			result.add(fromColumnSlice(row.getKey(), row.getColumnSlice(), Battle.class));
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
	
	public void setText(String text){
		this.text = text;
		this.textModified = true;
	}
	
	public String getText(){
		return text;
	}
	
	protected Mutator<UUID> generateMutator(){
		Mutator<UUID> mutator = HFactory.createMutator(keyspace(), JugUuidSerializer.get());
		
		if (textModified){
			mutator.addInsertion(uuid, BattleTable, 
					HFactory.createStringColumn(
							"text", text));
			textModified = false;
		}
		
		if (outcomeModified){
			mutator.addInsertion(uuid, BattleTable,
					HFactory.createStringColumn(
							"outcome",
							"["
							+ com.google.common.base.Joiner.on(',')
							.join(outcome) + "]"));
			outcomeModified = false;
		}
		
		if (statusModified){
			mutator.addInsertion(uuid, BattleTable,
					HFactory.createStringColumn(
							"status", status.toString()));
			statusModified = false;
		}
		
		if (userGameAwardsModified){
			mutator.addInsertion(uuid,BattleTable, 
					HFactory.createStringColumn(
							"user_game_awards", user_game_awards.toJson()
							));
			userGameAwardsModified = false;
		}
		
		if (userAwardsModified){
			mutator.addInsertion(uuid,BattleTable, 
					HFactory.createStringColumn(
							"user_awards", user_awards.toJson()
							));
			userAwardsModified = false;
		}
		
		if (userEventAwardsModified){
			mutator.addInsertion(uuid,BattleTable, 
					HFactory.createStringColumn(
							"user_event_awards", user_event_awards.toJson()
							));
			userEventAwardsModified = false;
		}
		
		return mutator;
	}
	
	public void update(){

		generateMutator().execute();
		
	}

}

package cn.gamemate.app.domain.arena;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.factory.HFactory;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import cn.gamemate.app.cassandra.Cassandra;
import cn.gamemate.app.domain.DomainModel;

import com.google.protobuf.GeneratedMessage.Builder;

//
//@Entity
//@RooJavaBean
//@RooToString
//@RooEntity
//@Table(name = "gm_battle")
@Configurable
public class Battle implements DomainModel{
	
	public enum BattleStatus {NEW, PENDING, DONE};
	
	protected static final Logger logger= LoggerFactory.getLogger(Arena.class);
	//protected List<ArenaSlot> players;
	
	protected proto.res.ResArena.Arena arenaSnapshot;
	protected BattleStatus status;
	protected static final String BattleTable =  "Battle";
	protected static final String UserBattleTable =  "UserBattles";
	
	@Autowired
	protected UUIDGenerator uuidGenerator;
	
	@Autowired
	protected Cassandra cassandra;
	
	private UUID uuid;
	
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	private static UUIDGenerator getUuidGenerator(){
		
		UUIDGenerator generator = new Battle().uuidGenerator;
		if (generator == null) throw new IllegalStateException("UUIDGenerator has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return generator;
	}
	private static Cassandra getCassandra(){
		
		Cassandra cassandra = new Battle().cassandra;
		if (cassandra == null) throw new IllegalStateException("Cassandra has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return cassandra;
	}
	private static String getCassandraUserIndexName(Integer gameid){
		if (gameid.equals(1)){
			return "UserWar3Battles";
		}else if (gameid.equals(2)){
			return "UserDotaBattles";
		}else if (gameid.equals(3)){
			return "UserScBattles";
		}return null;
	}


	public static Battle createAndSave(proto.res.ResArena.Arena arenaStart){
		Battle battle = new Battle();
		battle.status = BattleStatus.NEW;
		battle.arenaSnapshot = arenaStart;
		battle.uuid = getUuidGenerator().generateTimeBasedUUID();
		
		//TODO: check not exist
		getCassandra().newMutator()
			.addInsertion(battle.uuid.toString(), BattleTable, 
					HFactory.createStringColumn(
							"status", battle.status.toString()))
			.addInsertion(battle.uuid.toString(), BattleTable, 
					HFactory.createColumn(
							"arenaSnapshot", battle.arenaSnapshot.toByteArray(),
							StringSerializer.get(), BytesArraySerializer.get()))
			.execute();
		
		for(proto.res.ResArena.ArenaSlot slot:arenaStart.getPlayersList()){
			if (slot.hasUser()){
				getCassandra().newMutator()
					.addInsertion(
							Integer.toString(slot.getUser().getId()), 
							getCassandraUserIndexName(arenaStart.getLogicalGame().getId()), 
							HFactory.createColumn(
									battle.uuid.asByteArray(),
									"{\"game\":"+Integer.toString(arenaStart.getLogicalGame().getId()) + "}",
									BytesArraySerializer.get(), StringSerializer.get()))
					.execute();
			}
		}
		return battle;
	}

	@Override
	public Builder toProtobuf() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

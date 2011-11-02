package cn.gamemate.app.domain.event.dota;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import org.safehaus.uuid.UUID;

import cn.gamemate.app.cassandra.JugUuidSerializer;
import cn.gamemate.app.domain.arena.Battle;

public class DotaBattle extends Battle {

	private String parsed_rep;
	private boolean parsedReplyModified = false;

	public String getParsedReply() {
		return parsed_rep;
	}
	
	public void setParsedReply(String data) {
		parsed_rep = data;
		parsedReplyModified = true;
	}
	
	private long effectiveLength;
	private boolean effectiveLengthModified = false;
	public long getEffectiveLength(){
		return effectiveLength;
		
	}
	public void setEffectiveLength(long effectiveLength){
		this.effectiveLength = effectiveLength;
		effectiveLengthModified = true;
	}
	

	private String adopted_rep;
	private boolean adoptedReplyModified = false;

	public String getAdoptedReply() {
		return adopted_rep;
	}
	public void setAdoptedRep(String submitter){
		adopted_rep = submitter;
		adoptedReplyModified = true;
	}
	
	

	public static <T extends DotaBattle> T get(UUID uuid, Class<T> cls) {

		SliceQuery<UUID, String, byte[]> query = HFactory.createSliceQuery(keyspace(), JugUuidSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
		ColumnSlice<String, byte[]> result = query.setKey(uuid).setColumnFamily(BattleTable).setRange("", "", false, 20).execute().get();		
		T battle = T.fromColumnSlice(uuid, result, cls);
		
		return battle;
	}
	
	
	protected static <T extends Battle> T data_fromColumnSlice(
			UUID uuid,
			ColumnSlice<String, byte[]> columnSlice,
			Class<T> cls){
		
		T battle = fromColumnSlice(uuid, columnSlice, cls);
		
		
		return battle;
	}
	
	@Override
	protected Mutator<UUID> generateMutator(){
		Mutator<UUID> mutator = super.generateMutator();
		if (parsedReplyModified){
			mutator.addInsertion(uuid, BattleTable, 
					HFactory.createStringColumn(
							"parsed_rep", parsed_rep));
			this.parsedReplyModified = false;
		}
		if (effectiveLengthModified){
			mutator.addInsertion(uuid, BattleTable, 
					HFactory.createColumn("effective_length",Long.toString(effectiveLength), 
							StringSerializer.get(),StringSerializer.get()));
			this.effectiveLengthModified = false;
		}
		
		if (adoptedReplyModified){
			mutator.addInsertion(uuid, BattleTable, 
					HFactory.createStringColumn(
							"adopted_rep", adopted_rep));
			this.parsedReplyModified = false;
		}
		
		return mutator;
	}
	
	
	
}

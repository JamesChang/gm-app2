package cn.gamemate.app.domain.arena;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.hibernate.hql.ast.tree.UpdateStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.xalan.internal.lib.Extensions;

import proto.res.ResArena;
import proto.response.ResGame.LogicalGame;
import proto.util.Util.StringDictItem;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelEvent;
import cn.gamemate.app.domain.DomainModelExtension;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.attr.Field;
import cn.gamemate.app.domain.event.Event;
import cn.gamemate.app.domain.game.Game;
import cn.gamemate.app.domain.game.GameMap;
import cn.gamemate.app.domain.game.PhysicalGame;
import cn.gamemate.app.domain.user.User;

/**
 * @author jameszhang
 *
 */
public class Arena implements Serializable, DomainModel{
	
	public enum ArenaStatus {CLOSED, OPEN, MATCHING, GAMING};

	protected static final long serialVersionUID = 1L;
	protected static final Logger logger= LoggerFactory.getLogger(Arena.class);
	protected static final BasicArenaRepository<Integer> repository 
		= new BasicArenaRepository<Integer>();
	protected Battle lastBattle=null;

	public Battle getLastBattle() {
		return lastBattle;
	}
	
	private final UUID uuid;
	private String name="";
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	protected GameMap gameMap;
	protected PhysicalGame physicalGame;
	protected Game game;
	protected String mode="";
	protected ArenaStatus status;
	protected AtomicBoolean bPrivate;
	protected String type;
	protected Event event; 
	
	protected final List<ArenaSlot> slots;
	protected final List<ArenaForce> forces;
	protected final Map<String, String> attributes;
	protected final Map<String, Field> userAttrDefs;
	private final List<DomainModelExtension> extensions;
	
	
	public Arena() {
		uuid = UUID.randomUUID();
		this.slots = new ArrayList<ArenaSlot>();
		this.forces = new ArrayList<ArenaForce>();
		this.attributes = new HashMap<String, String>(10);
		this.userAttrDefs = new HashMap<String, Field>(10);
		this.extensions = new ArrayList<DomainModelExtension>();
		status=ArenaStatus.OPEN; 
		bPrivate = new AtomicBoolean(false);
		type="normal";
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	

	public static Arena fromPrototype(Arena arena) {
		return null;

	}

	public static Arena findArena(UUID uuid) {
		return null;
	}
	
	public static Arena findArena(Integer id) {
		return repository.get(id);
	}
	
	public List<ArenaForce> getForces(){
		return forces;
	}
	public ArenaForce addForce(ArenaForce force){
		if (force.getArena()!=null)
			throw new DomainModelRuntimeException("force has been added to other arena");
		if (forces.contains(force))
			throw new DomainModelRuntimeException("duplicated arena force");
		this.forces.add(force);
		int i =this.forces.indexOf(force);
		force.id = i;
		force.arena = this;
		return force;
	}
	public ArenaForce addForce(String label){
		ArenaForce force = new ArenaForce();
		force.label = label;
		return addForce(force);
	}
	public ArenaForce addForce(){
		return addForce("AnonymousForce");
	}
	
	public List<ArenaSlot> getSlots(){
		return slots;
	}
	
	public void save(){
		repository.add(getInt32Id(), this);
	}


	public GameMap getGameMap() {
		return gameMap;
	}

	public void setGameMap(GameMap gameMap) {
		this.gameMap = gameMap;
	}

	public PhysicalGame getPhysicalGame() {
		return physicalGame;
	}

	public void setPhysicalGame(PhysicalGame physicalGame) {
		this.physicalGame = physicalGame;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public UUID getUuid() {
		return uuid;
	}
	
	public ArenaStatus getStatus(){
		return status;
	}
	
	public boolean isPrivate(){
		return bPrivate.get();
	}
	public boolean setPrivate(boolean bPrivate){
		boolean oldPrivate=this.bPrivate.getAndSet(bPrivate);
		fireEvent(new ArenaStatusChangedEvent(this, status));
		return oldPrivate;
	}
	
	
	public String getType(){
		return type;
	}
	
	/**
	 * Latency
	 * 
	 * @return
	 */
	public int getInt32Id(){
		return (int) (getUuid().getLeastSignificantBits()>>>33);
	}
	
	

	public ArenaSlot getUserSlot(User operator) {
		for(ArenaSlot slot:slots){
			if (operator.equals(slot.getUser())){
				return slot;
			}
		}
		return null;
	}

	private static final List<String> normalActons = new ArrayList<String>();
	static{
		normalActons.add("chslot");
		normalActons.add("leave");
		normalActons.add("chat");
		normalActons.add("setUserAttr");
		normalActons.add("ready");
	}

	public List<String> getUserAvailableActions(User user){

		return normalActons;
		
	}
	
	
	/**
	 * get id list of all the users in this arena, including players, referees, ghosts.
	 * 
	 * @param receivers
	 * @return
	 */
	public List<Integer> setUserIdList(List<Integer> receivers){
		for(ArenaSlot slot: slots){
			if (slot.getUser()!= null){
				receivers.add(slot.getUser().getId());
			}
		}
		return receivers;
	}
	
	public String getSlotCountString(){
		int i=0;
		for(ArenaSlot slot:slots){
			if (slot.getUser()!= null) ++i;
		}
		return new StringBuilder().append(i).append('/').append(slots.size()).toString();
		
	}
	public int getPlayerCount(){
		int i=0;
		for(ArenaSlot slot:slots){
			if (slot.getUser()!= null) ++i;
		}
		return i;
	}
		
	
	public ResArena.Arena.Builder toProtobuf(){
		ResArena.Arena.Builder builder = ResArena.Arena.newBuilder();
		copyTo(builder);
		return builder;
	}
	
	public void copyTo(ResArena.Arena.Builder builder){
		builder.setId(getInt32Id());
		builder.setName(getName());
		builder.setUuid(uuid.toString());
		builder.setStatus(status.toString());
		builder.setType(type);
		builder.setPrivateFlag(isPrivate());
		if (event != null){
			builder.setEventName(event.getName()).setEventId(event.getId().toString());
		}
		if (mode != null) builder.setMode(mode);
		
		//write game
		if (game != null){
			proto.response.ResGame.LogicalGame.Builder lgBuilder = LogicalGame.newBuilder().setId(game.getId()).setName(game.getName());
			builder.setLogicalGame(lgBuilder);
		}
		
		if (physicalGame != null){
			proto.response.ResGame.PhysicalGame.Builder pgBuilder = proto.response.ResGame.PhysicalGame.newBuilder();
			pgBuilder.setId(physicalGame.getId())
				.setName(physicalGame.getName())
				.setVersion(proto.response.ResGame.PhysicalGameVersion.newBuilder()
					.setCode(physicalGame.getVersion())
					.setDigest(physicalGame.getMainFileDigest())
					.setFile(physicalGame.getMainFileName())
					.setDisplay(physicalGame.getVersionDisplay())
				);
			builder.setPhysicalGame(pgBuilder);
		}
		
		if (getGameMap()!=null){
			builder.setGameMap(this.getGameMap().toProtobuf());
		}
		
		//write slots 
		for (ArenaSlot slot :this.slots){
			ResArena.ArenaSlot.Builder slotBuilder = ResArena.ArenaSlot.newBuilder();
			if (slot.getUser()!=null){
				slotBuilder.setUser(slot.getUser().toProtobuf());
			}
			slotBuilder.setReady(slot.isReady());
			slotBuilder.setEnabled(slot.isEnabled());
			slotBuilder.setPosition(slot.getPosition());
			slotBuilder.setForceID(slot.getForce().getId());
			for(Entry<String, String> entry: slot.getExtra().entrySet()){
				StringDictItem.Builder item = StringDictItem.newBuilder();
				item.setKey(entry.getKey());
				item.setValue(entry.getValue());
				slotBuilder.addAttributes(item);
			}
			builder.addPlayers(slotBuilder);
		}
		
		for (ArenaForce force :this.forces){
			builder.addForces(force.toProtobuf());
		}
		
		//dotamode trick
		
		if (game.getId() == 2) {
		
			getAttributes().put("dotamode", mode);
		} else if (game .getId() == 3) {
			getAttributes().put("scmode", "TopVsBottom");
		}
		
		//set global attributes
		for(Entry<String, String> entry: attributes.entrySet()){
			builder.addAttributes(StringDictItem.newBuilder()
				.setKey(entry.getKey()).setValue(entry.getValue()));
		}
		//hostID trick
		if (attributes.containsKey("hostID")){
			builder.setHostID(Integer.parseInt(attributes.get("hostID")));
		}

		

		
		
	}

	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public void addExtension(ArenaExtension x){
		this.extensions.add(x);
	}
	public void addAllExtensions(Collection<ArenaExtension> xList){
		this.extensions.addAll(xList);
	}
	
	public void fireEvent(DomainModelEvent e){
		for(DomainModelExtension x:extensions){
			x.handleEvent(e);
		}
	}
	
	public void close(){
		status = ArenaStatus.CLOSED;
		fireEvent(new ArenaClosedEvent(this));
		for(ArenaSlot slot:slots){
			slot.clear();
		}
		repository.remove(getInt32Id());
	}
	
	public Map<String, String> getAttributes(){
		return attributes;
	} 
	
	
		

}

package cn.gamemate.app.domain.party;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.safehaus.uuid.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.response.ResParty;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.DomainModelEvent;
import cn.gamemate.app.domain.DomainModelExtension;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.event.Party;
import cn.gamemate.app.domain.user.AlertMessage;
import cn.gamemate.app.domain.user.User;


@Configurable
public class DefaultParty implements Serializable, DomainModel, Party{
	
	protected static final long  serialVersionUID = 1L;
	protected static final Logger logger= LoggerFactory.getLogger(DefaultParty.class);
	protected static final int max_user = 5;
	
	@Autowired 
	PartyManager partyManager;
	
	protected UUID uuid;
	protected final List<PartyMember> members;
	protected final Map<String, String> attributes;
	private final List<DomainModelExtension> extensions;
	private Integer leaderid;
	private AtomicInteger version; 
	
	
	public DefaultParty() {
		
		this.members = new ArrayList<PartyMember>(5);
		this.attributes = new HashMap<String, String>();
		this.extensions = new ArrayList<DomainModelExtension>();
		this.version = new AtomicInteger();
	}
	
	
	
	public UUID getUuid() {
		return uuid;
	}
	public List<PartyMember> getMembers(){
		return members;
	}
	public int getVersion(){
		return version.get();
	}



	@Override
	public ResParty.PartyModel.Builder toProtobuf() {
		ResParty.PartyModel.Builder builder= ResParty.PartyModel.newBuilder();
		copyTo(builder);
		return builder;
	}
	public void copyTo(ResParty.PartyModel.Builder builder){
		builder.setUuid(uuid.toString());
		builder.setLeaderID(leaderid);
		builder.setUserCount(members.size());
		for (PartyMember m: members){
			builder.addUsers(m.toProtobuf());
		}
	}

	public PartyMember addUser(User user){
		return addUser(user, true);
	}
	public PartyMember addUser(User user, boolean setUserFlag){
		if (members.size() >= max_user){
			throw new DomainModelRuntimeException("party member count limit exceeded.");
		}
		if (contains(user)){
			throw new DomainModelRuntimeException("target user is already in.");
		}
		PartyMember wrapper  = new  PartyMember(user);
		if (setUserFlag)
			user.casPartyId(uuid);
		members.add(wrapper);
		modified();
		fireEvent(new PartyChangedSignal(this));
		return wrapper;
	}
	
	// Should be thread safe
	public void removeUser(User user, boolean setUserFlag){
		modified();
		for (Iterator<PartyMember> iter = members.iterator();iter.hasNext();){
			PartyMember member = iter.next();
			if (member.getUser().equals(user)){
				if (setUserFlag)
					user.cacPartyId(uuid);
				iter.remove();
			}
		}
		fireEvent(new PartyChangedSignal(this));
	}
	public void removeUser(User user){
		removeUser(user, true);
	}
	
	public boolean contains(User user){
		for (PartyMember m: members){
			if (m.getUser().equals(user)){
				return true;
			}
		}
		return false;
	}
	
	public void setLeader(User user){
		modified();
		leaderid = user.getId();
	}
	public Integer getLeaderId(){
		return leaderid;
	}
	public PartyMember getLeaderSlot(){
		for (PartyMember m: members){
			if (m.getUser().getId() == leaderid){
				return m;
			}
		}
		return null;
	}
	public PartyMember getUserSlot(User user){
		for (PartyMember m: members){
			if (m.getUser().equals(user)){
				return m;
			}
		}
		return null;
	}


	public void setReceivers(List<Integer> receivers) {
		for (PartyMember m: members){
			if (!m.isOut())
				receivers.add(m.getUser().getId());
		}
	}
	

	protected void autoClose() {
		if (members.size() <= 1) {
			close();
		}
	}

	protected void autoElectLeader(User operator) {
		if (operator.getId() == leaderid){
			if (members.size() >1){
				leaderid = members.get(0).getUser().getId();
				modified();
				new PartyLeaderChanged(this, leaderid).send();
			}
		}
		
	}
	public void fireEvent(DomainModelEvent e){
		for(DomainModelExtension x:extensions){
			x.handleEvent(e);
		}
	}
	
	public void assertPartyLeader(User user){
		if (leaderid != user.getId()){
			throw new DomainModelRuntimeException("Permission Denied.");
		}
	}
	
	///////////////////////////////////////
	//  Service
	//////////////////////////////////////
	
	synchronized public void close(){
		fireEvent(new PartyClosedSignal(this));
		ArrayList<PartyMember> MembersCopy = new ArrayList<PartyMember>(members);
		for(PartyMember slot:MembersCopy){	
			try{
				slot.getUser().cacPartyId(uuid);
				new PartyLeavedMessage(this, slot.getUser()).send();
			}catch(DomainModelRuntimeException e){
				
			}
		}
		partyManager.removeIndex(this);
		modified();
	}

	
	synchronized public void userLeave(User operator){
		removeUser(operator);
		new PartyLeavedMessage(this, operator).send();
		new UserLeavedPartyMessage(this, operator).send();
		autoElectLeader(operator);
		autoClose();
	}
	
	synchronized public void userKick(User operator, User target){
		assertPartyLeader(operator);
		removeUser(target, false);
		new UserLeavedPartyMessage(this, target).send();
		autoClose();
		autoElectLeader(operator);
		try{
			target.cacPartyId(uuid);
			new PartyLeavedMessage(this, target).send();
			new AlertMessage(target, "您被踢出团队", true).send();
		}catch(DomainModelRuntimeException e){
			
		}
	}
	
	synchronized public void userInviteUser(User leader, User target){
		PartyMember slot = addUser(target, false);		
		new UserJoinedPartyMessage(this, this.getUserSlot(target)).send();
		new PartyInvitationMessage(this, slot.getUser()).send();
	}
	synchronized public void userInviteUser(User leader, List<User> targets){
		for (User u : targets){
			try{
				PartyMember partyMember = addUser(u, false);
				new UserJoinedPartyMessage(this, partyMember).send();
				new PartyInvitationMessage(this, u).send();
				
			}catch(DomainModelRuntimeException e){
				continue;
			}
		}
	}



	synchronized  public void userChat(User operator, String content) {
		PartyMember userSlot = getUserSlot(operator);
		if (userSlot == null) {
			throw new DomainModelRuntimeException("not in this arena");
		}
		new PartyChatMessage(this, operator, content).send();
		
	}



	@Override
	public int getPlayerCount() {
		return members.size();
	}


	public void addExtension(PartyExtension x){
		this.extensions.add(x);
	}
	public void removeExtension(PartyExtension x){
		this.extensions.remove(x);
	}
	private void modified(){
		version.incrementAndGet();
		fireEvent(new PartyChangedSignal(this));
	}
	
	
}

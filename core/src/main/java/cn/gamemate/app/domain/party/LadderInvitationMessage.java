package cn.gamemate.app.domain.party;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import proto.msg.MsgParty.EventInvitation;

import cn.gamemate.app.clientmsg.AnswerableClientMessage;
import cn.gamemate.app.clientmsg.ClientMessage;
import cn.gamemate.app.domain.DomainModelRuntimeException;
import cn.gamemate.app.domain.arena.UserSlot;
import cn.gamemate.app.domain.event.Event;
import cn.gamemate.app.domain.event.Ladder;
import cn.gamemate.app.domain.user.AlertMessage;
import cn.gamemate.app.domain.user.User;

@Configurable
public class LadderInvitationMessage extends AnswerableClientMessage{

	static final Logger logger = LoggerFactory
			.getLogger(PartyInvitationMessage.class);
	
	Ladder ladder;
	DefaultParty party;
	int partyVersion;
	
	@Autowired
	PartyManager partyManager;
	
	
	public LadderInvitationMessage(final DefaultParty iParty, final Ladder iLadder) {
		party = iParty;
		partyVersion = iParty.getVersion();
		party.setReceivers(receivers);
		receivers.remove(party.getLeaderId());
		ladder = iLadder; 
		rootBuilder.setEventInvitation(
				EventInvitation.newBuilder().setEventID(ladder.getId()));
	}

	@Override
	public int getCode() {
		return 0x270B;
	}
	@Override
	public int getAge() {
		return 31;
	}
	
	
	synchronized 
	protected void answerCallback(User user, String answer) {
		DefaultParty party = partyManager.getParty(user);
		if (party == null)
			return;
		PartyMember partyMember = party.getUserSlot(user);
		if (partyMember == null) return;
		partyMember.setWaited(false);
		if (answer.equals("no")){
			ladder.remove(party);
			ArrayList<Integer> receivers = new ArrayList<Integer>();
			for (UserSlot userSlot: party.getMembers()){
				if (userSlot.getUser().getId() != user.getId())
					receivers.add(userSlot.getUser().getId());
			}
			tag = "no";
			new LadderInvitationDeclinedMessage(receivers, party, user.getName()+" 拒绝了这次游戏").send();
		}else{
			int i = answer.indexOf(61); //search for '='
			if (i == -1){
				return;
			}
			String k = answer.substring(0, i);
			String v = answer.substring(i+1);
			synchronized(party){
				PartyMember userSlot = party.getUserSlot(user);
				if (userSlot == null){
					return;
				}
				userSlot.setAttribute(k, v);
			}
			
			if (answeredUsers.size() == receivers.size()){
				tag = "yes";
				ladder.add(party);
				new PartyMemberUpdateMessage(party).send();
			}
			
		}
		new PartyMemberUpdateMessage(party,partyMember).send();
		
	}

	@Override
	protected void onTimeOut() {
		if (tag != null)
			return;
		if (party.getVersion() != partyVersion){
			throw new DomainModelRuntimeException("Party has changed since last invitation.");
		}
		ladder.remove(party);
		ArrayList<Integer> receivers = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();
		receivers.add(party.getLeaderId());
		for (PartyMember userSlot: party.getMembers()){
			if (answeredUsers.contains(userSlot.getUser().getId())){
				receivers.add(userSlot.getUser().getId());
			}else if (userSlot.getUser().getId() != party.getLeaderId()){
				sb.append(" ").append(userSlot.getUser().getName());
				userSlot.setWaited(false);
				new PartyMemberUpdateMessage(party,userSlot).send();
			}
		}
		sb.append("没有回应");
		new LadderInvitationDeclinedMessage(receivers, party, sb.toString()).send();
		
	}

}

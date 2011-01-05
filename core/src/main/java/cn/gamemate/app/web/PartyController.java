package cn.gamemate.app.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.event.Arena05;
import cn.gamemate.app.domain.party.DefaultParty;
import cn.gamemate.app.domain.party.PartyManager;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;
import cn.gamemate.app.web.ArenaController.MyArenaList;

import com.google.common.base.Splitter;

@RequestMapping("/party")
@Controller
public class PartyController {

	@Autowired(required = true)
	PartyManager partyManager;

	@Autowired(required = true)
	UserRepository userRepository;

	@RequestMapping(value = "/create")
	public String create(@RequestParam Integer userid,
			@RequestParam String members) {
		User user = userRepository.getUser(userid);
		Iterable<String> memberIds = Splitter.on(',').split(members);
		List<User> users = new ArrayList<User>();
		for (String memberId : memberIds) {
			User m = userRepository.getUser(Integer.valueOf(memberId));
			users.add(m);
		}
		partyManager.userCreateParty(user, users);
		return "";
	}

	@RequestMapping(value = "/quit")
	public String leave(@RequestParam Integer userid) {
		User user = userRepository.getUser(userid, false);
		if (user != null) {
			DefaultParty party = partyManager.getParty(user);
			if (party != null) {
				party.userLeave(user);
				return "";
			}
		}
		partyManager.sendPartyLeavedMessage(userid);
		return "";
	}
	
	@RequestMapping(value = "/kick")
	public String kick(@RequestParam Integer userid, @RequestParam Integer targetid){
		User operator = userRepository.getUser(userid, false);
		User target = userRepository.getUser(targetid, false);
		DefaultParty party = partyManager.getParty(operator);
		party.userKick(operator, target);
		return "";
	}
	
	@RequestMapping(value = "/invite")
	public String invite(@RequestParam Integer userid,
			@RequestParam String members) {
		User operator = userRepository.getUser(userid);
		Iterable<String> memberIds = Splitter.on(',').split(members);
		List<User> users = new ArrayList<User>();
		for (String memberId : memberIds) {
			User m = userRepository.getUser(Integer.valueOf(memberId));
			users.add(m);
		}
		DefaultParty party = partyManager.getParty(operator);
		party.userInviteUser(operator, users);
		return "";
	}
	
	@RequestMapping(value="/chat")
	public String chat(@RequestParam Integer userid, @RequestParam String content){
    	User operator = userRepository.getUser(userid);
    	DefaultParty party = partyManager.getParty(operator);
    	party.userChat(operator, content);
    	return "";
    }

	@RequestMapping(value="/mine")
	public String mine(@RequestParam Integer userid, ModelMap modelMap){
    	User operator = userRepository.getUser(userid);
    	DefaultParty party = partyManager.getParty(operator, false);
    	if (party == null) return "";
    	modelMap.addAttribute("object", party);
        modelMap.addAttribute("subpb", "partyGet");
    	return "";
    }
	

}

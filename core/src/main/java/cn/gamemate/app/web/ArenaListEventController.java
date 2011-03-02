package cn.gamemate.app.web;

import java.util.Iterator;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gamemate.app.domain.event.EventCenter;
import cn.gamemate.app.domain.event.Hall;
import cn.gamemate.app.domain.event.Ladder;
import cn.gamemate.app.domain.party.DefaultParty;
import cn.gamemate.app.domain.party.PartyManager;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;

import com.google.common.base.Splitter;


@RequestMapping("/events")
@Controller
public class ArenaListEventController {
	
	@Autowired
	private EventCenter eventCenter;
	@Autowired(required=true)
	private UserRepository userRepository;
	
	@Autowired(required=true)
	private PartyManager partyManager;
	
	@RequestMapping(value="/{id}/list")
	public String list( @PathVariable("id") Integer eventId, @RequestParam(required=false) String stick, ModelMap modelMap){
        modelMap.addAttribute("object", eventCenter.getEvent(eventId).getArenaList(stick));
        modelMap.addAttribute("subpb", "ca03ArenaList");
        return "";
	}
	
	@RequestMapping(value="/{id}")
	public String get( @PathVariable("id") Integer eventId, ModelMap modelMap){
		if (eventId<=3){
			modelMap.addAttribute("object", eventCenter.getEvent(eventId));
		}
		else{
			modelMap.addAttribute("object", eventCenter.getLadder(eventId));
		}
        
        modelMap.addAttribute("subpb", "eventGet");
        return "";
	}
	
	@RequestMapping(value="/{id}/create")
	public String create(@RequestParam(value="userid") Integer userId, 
			@PathVariable("id") Integer eventId, 
			@RequestParam(required=false) String mode, 
			@RequestParam(value="map", required=false) Integer mapId, 
			@RequestParam(value="private", required=false) boolean isPrivate,
			@RequestParam(value="attributes", required=false) String leaderAttributes,
			@RequestParam(value="name", required=false) String name,
			ModelMap modelMap){
		
		if (eventId<=100){
			Hall event = eventCenter.getEvent(eventId);
			User operator = userRepository.getUser(userId);
			DefaultParty party = partyManager.getParty(operator, false);
			if (party == null){
				event.userCreateArena(operator, mode, mapId, name);
			}
			else{
				event.partyCreateArena(operator, mode, mapId, name, false);
			}
			
			
		}else{
			Ladder event = eventCenter.getLadder(eventId);
			User operator = userRepository.getUser(userId);
			TreeMap<String, String> leaderAttrMap = new TreeMap<String, String>();
			if (leaderAttributes !=null && !leaderAttributes.equals("")){
				Iterator<String> iterator = Splitter.on('=').split(leaderAttributes).iterator();
				while(true){
					if (!iterator.hasNext()) break;
					String k = iterator.next();
					System.out.println(k);
					if (!iterator.hasNext()) break;
					String v = iterator.next();
					System.out.println(v);
					leaderAttrMap.put(k, v);
				}
			}
			DefaultParty party = partyManager.getParty(operator, false);
			if (party == null){
				event.singleJoin(operator, mode, leaderAttrMap);
			}
			else{
				event.partyJoin(operator, mode, leaderAttrMap);
			}
			
		}
		
		return "";
		
	}
	@RequestMapping(value="/{id}/leave")
	public String leave(@RequestParam(value="userid") Integer userId, @PathVariable("id") Integer eventId){
		Ladder event = eventCenter.getLadder(eventId);
		User operator = userRepository.getUser(userId);
		event.userLeave(operator);
		return "";
	}
	

}

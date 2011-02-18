package cn.gamemate.app.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import proto.res.ResArena.ArenaList;
import proto.response.ResCampusArena.CampusArena03List;
import proto.response.ResCampusArena.CampusArena03ListItem;

import com.google.common.base.Splitter;
import com.google.protobuf.GeneratedMessage.Builder;
//import com.springsource.insight.annotation.InsightOperation;

import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.ObjectNotFound;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.arena.msg.ArenaLeavedMessage;
import cn.gamemate.app.domain.event.Arena05;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;
import sun.misc.BASE64Decoder;

@RequestMapping("/arenas")
@Controller
public class ArenaController {
	
	@Autowired(required=true)
	UserRepository userRepository;
	
	
	private void assertArena(Arena a, Integer id){
		if (a == null){
			throw new ObjectNotFound(Arena.class, id); 
		}
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Integer id, ModelMap modelMap) {
    	Arena a = Arena.findArena(id);
    	assertArena(a, id);
        modelMap.addAttribute("object", Arena.findArena(id));
        modelMap.addAttribute("subpb", "arenaGet");
        return "";
    }
    
    @RequestMapping(value = "/{id}/enter")//, method = RequestMethod.GET)
    public String enter(@PathVariable("id") Integer id, @RequestParam Integer userid, ModelMap modelMap){
    	
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	a.userEnter(operator);
    	return "";
    }
    
    @RequestMapping(value = "/{id}/leave")
    public String leave(@PathVariable Integer id, @RequestParam Integer userid, ModelMap modelMap){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	if (a==null){
    		//TODO: move this to domain model
    		User operator = userRepository.getUser(userid, false);
    		if (operator!=null){
    			operator.setArenaId(null);
    		}
    		else{
    			operator = new User(userid, "tempuser", "");
    		}
    		new ArenaLeavedMessage(id, operator).send();
    		return "";
    	}
    	User operator = userRepository.getUser(userid);
    	
    	a.userLeave(operator);
    	return "";
    }
    

    @RequestMapping(value = "/{id}/chslot")
    public String changeSlot(@PathVariable Integer id, @RequestParam Integer userid, @RequestParam Integer seat,ModelMap modelMap){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	a.userChangeSlot(operator, seat);
    	return "";
    }
    
    @RequestMapping(value = "/{id}/ready")
    public String ready(@PathVariable Integer id, @RequestParam Integer userid,ModelMap modelMap){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	//a.userReady(operator);
    	doReady(a,operator);
    	return "";
    }
    //@InsightOperation
    public void doReady(Arena05 a, User operator){
    	a.userReady(operator);
    }
    

    @RequestMapping(value = "/{id}/start")
    public String start(@PathVariable Integer id, @RequestParam Integer userid,ModelMap modelMap){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User user = userRepository.getUser(userid);
    	a.userStart(user);
    	return "";
    }
    
    @RequestMapping(value = "/{id}/setattr")
    public String setAttribute(@PathVariable Integer id, @RequestParam Integer userid,
    		@RequestParam String key, @RequestParam String value,
    		ModelMap modelMap){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	a.userSetAttribute(operator, key, value);
    	return "";
    }
    
    @RequestMapping(value = "/{id}/setattr_arena")
    public String setArenaAttribute(@PathVariable Integer id, @RequestParam Integer userid,
    		@RequestParam String key, @RequestParam String value,
    		ModelMap modelMap){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	a.userSetArenaAttribute(operator, key, value);
    	return "";
    }
    
    @RequestMapping(value= "/{id}/error")
    public String quitGame(@PathVariable Integer id, @RequestParam Integer userid,
    		ModelMap modelMap){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	a.userError(operator);
    	return "";
    }
    
    @RequestMapping(value="/{id}/chat")
    public String chat(@PathVariable Integer id, @RequestParam Integer userid, @RequestParam String content){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	a.userChat(operator, content);
    	return "";
    }
    
    @RequestMapping(value="/{id}/submit_result")
    public String sbmt_rst(@PathVariable Integer id, @RequestParam Integer userid, @RequestParam String result) throws IOException{
    	byte[] decodedResult=null;
		//try {
    		BASE64Decoder b64decoder = new BASE64Decoder();
			decodedResult = b64decoder.decodeBuffer(result);
		//} catch (IOException e) {
		//	throw new RuntimeException("cannot parse game result. ",e);
		//}
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	a.userSubmitResult(decodedResult);
    	
    	return "";
    }

    @RequestMapping(value="/{id}/submit_result_le")
    public String sbmt_rst(@PathVariable Integer id, @RequestParam Integer userid, @RequestParam(value="winner") Integer winnerForce) throws IOException{
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	a.userSubmitResultL(winnerForce);
    	return "";
    }
    
    
    @RequestMapping(value="/{id}/kick")
    public String kick(@PathVariable Integer id, @RequestParam Integer userid, @RequestParam Integer targetid){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	
    	User operator = userRepository.getUser(userid);
    	User target = userRepository.getUser(targetid);
    	a.userKick(operator, target);
    	return "";
    }
    
    @RequestMapping(value="/{id}/lockslot")
    public String lockSlot(@PathVariable Integer id, @RequestParam Integer userid, @RequestParam Integer slotid){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	
    	User operator = userRepository.getUser(userid);
    	a.userLockSlot(operator, slotid);
    	return "";
    }
    
    @RequestMapping(value="/{id}/unlockslot")
    public String unlockSlot(@PathVariable Integer id, @RequestParam Integer userid, @RequestParam Integer slotid){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	
    	User operator = userRepository.getUser(userid);
    	a.userUnlockSlot(operator, slotid);
    	return "";
    }
    
    @RequestMapping(value="/{id}/cancel")
    public String cancel(@PathVariable Integer id, @RequestParam Integer userid){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	User operator = userRepository.getUser(userid);
    	a.userCancel(operator);
    	return "";
    }
    
    @RequestMapping(value="/{id}/invite")
    public String invite(@PathVariable Integer id, @RequestParam Integer userid, @RequestParam String users){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	assertArena(a, id);
    	
    	User operator =userRepository.getUser(userid);
    	Iterable<String> userids = Splitter.on(',').split(users);
    	for (String targetid: userids){
    		//TODO: Handle runtime exceptions here 
    		User target =userRepository.getUser(Integer.valueOf(targetid));
    		a.userInvite(operator, target);
    	}
    	return "";
    }
    

	class MyArenaList implements DomainModel {
		
		private Arena arena;
		private User user;

		public MyArenaList(Arena arena, User user) {
			this.arena = arena;
			this.user = user;
		}

		@Override
		public Builder toProtobuf() {
			ArenaList.Builder pbList = ArenaList.newBuilder();
			proto.res.ResArena.Arena.Builder protobuf = arena.toProtobuf();
			List<String> userAvailableActions = arena.getUserAvailableActions(user);
			for (String action: userAvailableActions){
				protobuf.addMyActions(action);
			}
			pbList.addArenas(protobuf);
			return pbList;
		}

	}
    
    @RequestMapping(value="/mine")
    public String mine(@RequestParam Integer userid, ModelMap modelMap){
    	User operator =userRepository.getUser(userid);
    	Integer arenaId = operator.getArenaId();
    	if (arenaId == null) return "";
    	Arena05 a = (Arena05)Arena.findArena(arenaId);
    	assertArena(a, arenaId);
    	modelMap.addAttribute("object", new MyArenaList(a, operator));
        modelMap.addAttribute("subpb", "arenaList");
    	return "";
    }
    
    @RequestMapping(value="/{id}/neterror")
    public String neterror(@PathVariable Integer id, @RequestParam String msg){
    	Arena05 a = (Arena05)Arena.findArena(id);
    	if (a==null){
    		return "";
    	}
    	a.netError(msg);
    	return "";
    }
    
    
}

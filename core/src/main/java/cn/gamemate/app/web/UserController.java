package cn.gamemate.app.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proto.response.ResUser.UserListResponse;
import proto.response.ResUser.UserModel;
import cn.gamemate.app.clientmsg.CompoundMessageService;
import cn.gamemate.app.clientmsg.MessageService;
import cn.gamemate.app.clientmsg.RelayServices;
import cn.gamemate.app.domain.DomainModel;
import cn.gamemate.app.domain.ObjectNotFound;
import cn.gamemate.app.domain.arena.Arena;
import cn.gamemate.app.domain.event.Arena05;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;

import com.google.common.base.Splitter;
import com.google.protobuf.GeneratedMessage.Builder;

@RequestMapping("/users")
@Controller
public class UserController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired(required=true)
	UserRepository userRepository;
	
	@Autowired(required=true)
	MessageService messageService;
	
	@RequestMapping(value = "/{id}/set_relay")
	public String setUserRelayService(@PathVariable("id") Integer userId,
			@RequestParam String service,
			ModelMap modelMap){
		User user = userRepository.getUser(userId);
		RelayServices.setRelayServiceForUser(user, service);
		logger.debug("set relay: {} {}", userId, service);
        return "";
	}
	
	@RequestMapping(value = "/{id}/reset_relay")
	public String resetUserRelayService(@PathVariable("id") Integer userId,
			@RequestParam String service,
			ModelMap modelMap){
		User user = userRepository.getUser(userId);
		RelayServices.clearRelayServiceForUser(user, service);
		userRepository.fireUserBrowseOnly(user);
		logger.debug("reset relay: {} {}", userId, service);
        return "";
	}
	
	@RequestMapping(value = "/{id}/login")
	public String userLogin(@PathVariable("id") Integer userId, 
			@RequestParam(value="msgname", required=false) String msgname, 
			ModelMap modelMap){
		userRepository.login(userId);
		logger.debug("user {} login ", userId);
		if (msgname !=null && !msgname.equals("")){
			CompoundMessageService service = (CompoundMessageService)messageService;
			service.specifyServiceForUser(userId, msgname);
		}
        return "";
	}
	
	@RequestMapping(value = "/{id}/drop")
	public String userDrop(@PathVariable("id") Integer userId, ModelMap modelMap){
		userRepository.drop(userId);
		logger.debug("user {} drop ", userId);
        return "";
	}
	
	@RequestMapping(value = "/{id}/logout")
	public String userLogout(@PathVariable("id") Integer userId, ModelMap modelMap){
		userRepository.drop(userId);
		logger.debug("user {} logout ", userId);
        return "";
	}
	
	class UserList implements DomainModel {
		private Iterable<String> userIdList;

		public UserList(Iterable<String> userIdList) {
			this.userIdList = userIdList;
		}

		@Override
		public Builder toProtobuf() {
			UserListResponse.Builder userList = UserListResponse.newBuilder();
			for (String id: userIdList){
				Integer uid=null;
				try{
					uid=Integer.parseInt(id);
				}catch(NumberFormatException e){
					continue;
				}
				User user = userRepository.getUser(uid, false);
				if (user !=null){
					
					proto.response.ResUser.UserModel.Builder userBuilder = UserModel.newBuilder()
					.setId(user.getId())
					.setName(user.getName())
					.setStatus(user.getStatus().toString().toLowerCase())
					.setIsInArena(user.getArenaId() != null)
					.setStatusEx(user.getStatusEx());
					if (user.getArenaId()!=null){
						userBuilder.setCampusArenaID(user.getArenaId());
					}
					userList.addUsers(userBuilder);
				}
			}
			return userList;
		}
		
	}
	
	@RequestMapping(value = "/online")
	public String userList(@RequestParam(value="users") String userIdString, ModelMap modelMap){
		Iterable<String> users = Splitter.on(',').split(userIdString);
		modelMap.addAttribute("object", new UserList(users));
		modelMap.addAttribute("subpb", "userList");
		return "";
	}
	
	@RequestMapping(value= "/{id}/follow")
	public String follow(@PathVariable("id") Integer targetId, @RequestParam Integer userid, ModelMap modelMap){
		User operator = userRepository.getUser(userid);
		User target = userRepository.getUser(targetId);
		if (target.getArenaId()!=null){
			Arena05 arena= (Arena05)Arena.findArena(target.getArenaId());
			if (arena == null){
				throw new ObjectNotFound(Arena.class, target.getArenaId()); 
			}
			arena.userEnter(operator);
		}
		return "";
		
	}
	
	

}

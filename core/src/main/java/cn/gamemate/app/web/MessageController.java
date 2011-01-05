package cn.gamemate.app.web;


import java.util.UUID;

import org.safehaus.uuid.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gamemate.app.clientmsg.AnswerableClientMessage;
import cn.gamemate.app.domain.user.User;
import cn.gamemate.app.domain.user.UserRepository;

@RequestMapping("/messages")
@Controller
public class MessageController {


	@Autowired(required=true)
	UUIDGenerator uuidGenerator;
	
	@Autowired(required=true)
	UserRepository userRepository;
	
    @RequestMapping(value = "/{id}/answer")
    public String answer(@PathVariable("id") String uuid, 
    		@RequestParam(required=true) Integer userid,
    		@RequestParam(required=true) String answer) {
    	AnswerableClientMessage message = AnswerableClientMessage.getMessage(UUID.fromString(uuid));
    	User user = userRepository.getUser(userid);
    	message.answer(user, answer);
    	return "";
    }
}

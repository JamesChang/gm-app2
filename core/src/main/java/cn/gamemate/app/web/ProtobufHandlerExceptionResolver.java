package cn.gamemate.app.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.gamemate.app.domain.DomainModelRuntimeException;

public class ProtobufHandlerExceptionResolver implements HandlerExceptionResolver{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
                if (ex instanceof DomainModelRuntimeException){
 
                }
                else{

		logger.error("", ex);
                }
		//System.out.println(ex.getStackTrace());
		ModelAndView mv = new ModelAndView("", "ex", ex);
		return mv;
		
	}

}

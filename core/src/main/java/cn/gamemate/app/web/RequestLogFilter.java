package cn.gamemate.app.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLogFilter implements Filter {
	
	private Logger logger;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger = LoggerFactory.getLogger("cn.gamemate.app.web.R");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		logger.debug("{} {}", httpRequest.getMethod(), httpRequest.getPathInfo());
		
	}

	@Override
	public void destroy() {
		
		
	}

}

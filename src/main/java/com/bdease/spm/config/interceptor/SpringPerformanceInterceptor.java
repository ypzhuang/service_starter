/**
 * created Feb 11, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.bdease.spm.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SpringPerformanceInterceptor extends HandlerInterceptorAdapter {
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("{} {} start ...",request.getMethod(),request.getRequestURI());
		long startTime = System.currentTimeMillis();
		request.setAttribute("s",startTime);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		long startTime = (long)request.getAttribute("s");
		request.removeAttribute("s");
		long endTime = System.currentTimeMillis();		
		log.info("{} {} end.({}ms)",request.getMethod(),request.getRequestURI(), (endTime-startTime));
	}
}

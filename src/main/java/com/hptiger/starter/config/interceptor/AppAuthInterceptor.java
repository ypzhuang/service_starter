/**
 * created Feb 11, 2019 by ypzhuang
 * 
 * App AppID & App Security Validator
 */

package com.hptiger.starter.config.interceptor;

import java.time.Duration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import  org.apache.commons.lang3.StringUtils;


public class AppAuthInterceptor extends HandlerInterceptorAdapter {
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	
	@Value("${app_api.header.appId}")
	private String appIdHeader;
	
	@Value("${app_api.header.appSecurity}")
	private String appSecurityHeader;
	
	@Value("${app_api.cache}")
	private Integer cache;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String appId = request.getHeader(appIdHeader);
		String appSecurity = request.getHeader(appSecurityHeader);
		log.info("AppID:{} request {} {}  ...", appId, request.getMethod(), request.getRequestURI());
		

		if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecurity)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("Headers: %s and %s shouldn't be null.", appIdHeader,appSecurityHeader));
			return false;
		}
		String security = stringRedisTemplate.opsForValue().get(appId);	
		if (!appId.equals(security)) {			
			if (!validate(appId, appSecurity)) {
				log.error("FORBIDDEN AppID:{} to request {} {}.", appId, request.getMethod(), request.getRequestURI());
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "not authrorized");
				return false;
			} else {
				stringRedisTemplate.opsForValue().set(appId, appSecurity, Duration.ofSeconds(cache));				
			}
		} 
		
		request.setAttribute("APP_ID",appId);
		return true;
				
	}
	
	private boolean validate(String appId, String appSecurity) {
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		request.removeAttribute("APP_ID");
	}
}

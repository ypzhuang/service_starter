/**
 * created Feb 25, 2019 by ypzhuang
 * 
 *  Programming register beans and get beans.
 */

package com.hptiger.starter.context;


import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;


@Component
@Lazy(false)
public class SpringContextBridge implements SpringContextBridgedService, ApplicationContextAware {
	
	protected  static final Logger log = LoggerFactory.getLogger(SpringContextBridge.class);

	private  static ApplicationContext applicationContext;
	
	public static SpringContextBridgedService services() {		
        return applicationContext.getBean(SpringContextBridgedService.class);
    }
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextBridge.applicationContext = applicationContext;
	}
	
	@Override
	public <T> T getService(Class<T> serviceType) {		
		return applicationContext.getBean(serviceType);
	}

	@Override
	public <T> T getService(String name, Class<T> serviceType) {		
		return applicationContext.getBean(name,serviceType);	
	}
	

	
	@PostConstruct
	public void init() {	
		
	}
	
	
	public <T> void registerBean(String beanName,Class<T> serviceType,Supplier<T> supplier) {
		if(applicationContext instanceof GenericWebApplicationContext ) {
			GenericWebApplicationContext gwAppContext = (GenericWebApplicationContext)applicationContext;
			gwAppContext.registerBean(beanName,serviceType,supplier);	
		}		
	}
	
	
	public <T> void registerBean(Class<T> serviceType,Supplier<T> supplier) {
		this.registerBean(null,serviceType,supplier);				
	}	
}

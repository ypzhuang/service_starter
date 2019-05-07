/**
 * created Feb 25, 2019 by ypzhuang
 * 
 *  Programming register beans and get beans.
 */

package com.bdease.spm.context;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

import com.bdease.spm.config.AliyunConfiguration;
import com.bdease.spm.service.impl.aliyun.SMSService;

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
	
	@Autowired
	private AliyunConfiguration aliyunConfigs;
	
	@PostConstruct
	public void init() {		
		List<String> orgCodes = aliyunConfigs.getAliyunConfigs()
				.parallelStream()
				.map(config -> config.getTenentCode())
				.collect(Collectors.toList());		
		for(String orgCode: orgCodes) {		
			SMSService service =  new SMSService(orgCode);			
			registerBean(orgCode, SMSService.class, () -> service);	
			log.debug("initialized sms service for:{}",orgCode);
		}
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

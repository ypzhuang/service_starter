package com.hptiger.starter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Arrays;


@Component
public class Bootstrap {
	private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);
	

	@PostConstruct
	public void bootstrap() {
		log.info("bootstrap in a product env? {}",isProd());

	}

	
    @Autowired
    private Environment environment;
    
	private boolean isProd() {
		return Arrays.stream(environment.getActiveProfiles()).anyMatch("prod"::equals);
	}	
}

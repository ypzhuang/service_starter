package com.hptiger.starter.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	
	@Scheduled(cron = "0 0/5 * * * ?")
	public void updateOrderStaticsForUsers() {
		log.debug("I'm a scheduler.");
		

	}
}

/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "app.queue")
@Configuration
@Data
@NoArgsConstructor
public class QueueConfiguration {
	List<Queue> queues;
	
	@Data
	@NoArgsConstructor
	public static class Queue {
		 String queueName;
		 List<String> subscribers;
	}
}

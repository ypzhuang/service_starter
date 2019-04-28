/**
 * created Mar 1, 2019 by ypzhuang
 * 
 * RedisMessagePublisher 功能描述
 */

package com.bdease.spm.queue;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import com.bdease.spm.context.SpringContextBridge;

@Component
public class RedisMessagePublisher implements MessagePublisher {
	
	private RedisTemplate<String, Object> redisTemplate;
	
	private ChannelTopic topic;

	public RedisMessagePublisher() {		
		this(MessagePublisher.QUEUE_VALIDATION_CODE);
	}

	@SuppressWarnings("unchecked")
	public RedisMessagePublisher(String topic) {
		this.redisTemplate =   SpringContextBridge.services().getService("redisTemplate", RedisTemplate.class);
		this.setTopic(MessagePublisher.QUEUE_VALIDATION_CODE);				
	}
	
	@Override
	public void publish(Serializable message) {		
		redisTemplate.convertAndSend(topic.getTopic(), message);		
	}

	protected ChannelTopic getTopic() {
		return topic;
	}

	private void setTopic(String topic) {
		this.topic = new ChannelTopic(topic);;
	}
}

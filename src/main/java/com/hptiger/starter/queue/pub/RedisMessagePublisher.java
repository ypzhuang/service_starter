/**
 * created Mar 1, 2019 by ypzhuang
 * 
 * RedisMessagePublisher 功能描述
 */

package com.hptiger.starter.queue.pub;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import com.hptiger.starter.queue.MessagePublisher;
import com.hptiger.starter.queue.msg.ValidationCodeMessage;

@Component("redis")
public class RedisMessagePublisher implements MessagePublisher {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	private ChannelTopic topic;

	public RedisMessagePublisher() {		
		this(ValidationCodeMessage.QUEUE_VALIDATION_CODE);
	}

	public RedisMessagePublisher(String topic) {		
		this.setTopic(topic);
	}
	
	@Override
	public void publish(Serializable message) {		
		redisTemplate.convertAndSend(topic.getTopic(), message);		
	}

	protected ChannelTopic getTopic() {
		return topic;
	}

	private void setTopic(String topic) {
		this.topic = new ChannelTopic(topic);
	}

	@Override
	public void publish(String topic, Serializable message) {		
		redisTemplate.convertAndSend(topic, message);		
	}
}

/**
 * created Mar 1, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.bdease.spm.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import com.bdease.spm.context.SpringContextBridge;
import com.bdease.spm.service.impl.aliyun.SMSService;


public class RedisMessageSMSSubscriber implements MessageListener {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String channel = new String(message.getChannel());
		log.debug("Message received: {} on topic: [{}]", message.toString(), channel);
		
		if(MessagePublisher.QUEUE_VALIDATION_CODE.equals(channel)) {					
			@SuppressWarnings("unchecked")
			RedisTemplate<String, Object> redisTemplate=  SpringContextBridge.services().getService("redisTemplate", RedisTemplate.class);
			Object obj = redisTemplate.getValueSerializer().deserialize(message.getBody());
						
			if(MessagePublisher.QUEUE_VALIDATION_CODE.equals(channel)) {			
				if(obj instanceof ValidationCodeMessage) {
					ValidationCodeMessage msg = (ValidationCodeMessage)obj;	
					SMSService smsService = SpringContextBridge.services().getService(msg.getFromTenentCode(), SMSService.class);
					smsService.sendMessage(msg.getTo(), msg.getTemplate(), msg.getParams());
					log.debug("SMS sent: {}",msg);					
				}
			}				
		}
	}

}

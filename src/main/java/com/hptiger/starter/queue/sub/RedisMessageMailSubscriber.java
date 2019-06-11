/**
 * created Mar 1, 2019 by ypzhuang
 * 
 * RedisMessageMailSubscriber
 */

package com.hptiger.starter.queue.sub;

import com.hptiger.starter.utils.RedisHelper;
import com.hptiger.starter.context.SpringContextBridge;
import com.hptiger.starter.queue.msg.ValidationCodeMessage;
import com.hptiger.starter.service.IMailService;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.gson.Gson;


public class RedisMessageMailSubscriber implements MessageListener {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	public final 
	Gson gson = new Gson();
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String channel = new String(message.getChannel());
		log.debug("Message received: {} on topic: [{}]",  message.toString(), channel);	
		IMailService mailService = SpringContextBridge.services().getService(IMailService.class);
		
		@SuppressWarnings("unchecked")
		RedisTemplate<String, Object> redisTemplate=  SpringContextBridge.services().getService("redisTemplate", RedisTemplate.class);
		Object obj = redisTemplate.getValueSerializer().deserialize(message.getBody());
		
		if(ValidationCodeMessage.QUEUE_VALIDATION_CODE.equals(channel)) {			
			if(obj instanceof ValidationCodeMessage) {
				ValidationCodeMessage msg = (ValidationCodeMessage)obj;				
				mailService.sendTemplateMessage(to(msg.getTo()), "验证码", msg.getTemplate(), msg.getParams());				
				RedisHelper.storeCodeInRedis(msg.getTo(), msg.getFromTenentCode(), msg.getParams());
			}
		}	
	}
	
	private String to(String to) {
		if (EmailValidator.getInstance(true).isValid(to)) {
			return to;
		} else {			
			return String.join("@",to ,"mailinator.com");
		}		
	}
}

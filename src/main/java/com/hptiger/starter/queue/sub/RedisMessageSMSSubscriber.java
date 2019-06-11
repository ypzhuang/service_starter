/**
 * created Mar 1, 2019 by ypzhuang
 * 
 * RedisMessageSMSSubscriber
 */

package com.hptiger.starter.queue.sub;

import com.hptiger.starter.context.SpringContextBridge;
import com.hptiger.starter.queue.msg.ValidationCodeMessage;
import com.hptiger.starter.service.impl.aliyun.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisMessageSMSSubscriber implements MessageListener {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String channel = new String(message.getChannel());
		log.debug("Message received: {} on topic: [{}]", message.toString(), channel);
		
		if (ValidationCodeMessage.QUEUE_VALIDATION_CODE.equals(channel)) {
			@SuppressWarnings("unchecked")
			RedisTemplate<String, Object> redisTemplate = SpringContextBridge.services().getService("redisTemplate",
					RedisTemplate.class);
			Object obj = redisTemplate.getValueSerializer().deserialize(message.getBody());

			if (obj instanceof ValidationCodeMessage) {
				ValidationCodeMessage msg = (ValidationCodeMessage) obj;
				SMSService smsService = SpringContextBridge.services().getService(msg.getFromTenentCode(),
						SMSService.class);
				smsService.sendMessage(msg.getTo(), msg.getTemplate(), msg.getParams());
				log.debug("SMS sent: {}", msg);			}

		}
	}

}

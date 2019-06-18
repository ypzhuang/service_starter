package com.hptiger.starter.queue.pub;

import java.io.Serializable;


import javax.annotation.Resource;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.hptiger.starter.queue.MessagePublisher;

@Component("rocketmq")
public class RocketMQMessagePublisher implements MessagePublisher {
	private static final Logger log = LoggerFactory.getLogger(RocketMQMessagePublisher.class);

	@Resource
    private RocketMQTemplate rocketMQTemplate;
	
	private String topic;
	
	public RocketMQMessagePublisher() {		
		this("starter-topic");
	}

	public RocketMQMessagePublisher(String topic) {		
		this.setTopic(topic);
	}
	
	@Override
	public void publish(Serializable message) {
		log.debug("try to send message on topic[{}]:{}",this.getTopic(), message);
		rocketMQTemplate.convertAndSend(this.getTopic(), message);		
	}

	@Override
	public void publish(String topic, Serializable message) {
		log.debug("try to send message on topic[{}]:{}",topic, message);
		rocketMQTemplate.convertAndSend(topic, message);		
	}
	
	public void publish(Message<?> message) {
		log.debug("try to send message on topic[{}]:{}",topic, message);
		rocketMQTemplate.send(topic, message);	
	}
	
	public void publish(String topic ,Message<?> message) {
		log.debug("try to send message on topic[{}]:{}",topic, message);
		rocketMQTemplate.send(topic, message);	
	}
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}

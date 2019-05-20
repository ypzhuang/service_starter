/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * subscribe topic 功能描述
 */

package com.hptiger.starter.queue;

import java.util.List;

import javax.annotation.PostConstruct;

import com.hptiger.starter.context.SpringContextBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import com.hptiger.starter.config.QueueConfiguration;
import com.hptiger.starter.config.QueueConfiguration.Queue;

@Service
public class SubscriberService {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	QueueConfiguration queueConfiguration;

	@Autowired
	private LettuceConnectionFactory lettuceConnectionFactory;

	@Autowired
	private SpringContextBridge springContextBridge;
	
	@PostConstruct
	public void init() {
		log.debug("Init Subscriber Service:");
		
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(lettuceConnectionFactory);
		
		List<Queue> queues = queueConfiguration.getQueues();
		
		for(Queue queue : queues) {			
			Topic topic = new ChannelTopic( queue.getQueueName());
			
			List<String> subscribers = queue.getSubscribers();
			for(String subscriber : subscribers) {
				try {
					log.debug("init subscriber:{}",subscriber);
					MessageListener listener = (MessageListener)Class.forName(subscriber).newInstance();
					MessageListener messageListener = new MessageListenerAdapter(listener);
					container.addMessageListener(messageListener, topic);
					log.debug(" registered {} on topic: [{}]", subscriber, topic.toString());
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {					
					e.printStackTrace();
				}			
			}			
		}
		springContextBridge.registerBean(RedisMessageListenerContainer.class, () -> container);		
	}
}

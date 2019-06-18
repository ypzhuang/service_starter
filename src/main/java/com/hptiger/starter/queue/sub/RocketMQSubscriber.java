package com.hptiger.starter.queue.sub;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import com.hptiger.starter.queue.msg.OrderPaidEvent;

@Component
public class RocketMQSubscriber {
	private static final Logger log = LoggerFactory.getLogger(RocketMQSubscriber.class);
	
	@Service
	@RocketMQMessageListener(topic = "test-topic-1", consumerGroup = "my-consumer_test-topic-1")
	public class MyConsumer1 implements RocketMQListener<String> {
		public void onMessage(String message) {
			log.info("received message from topic test-topic-1: {}", message);
		}
	}

	@Service
	@RocketMQMessageListener(topic = "test-topic-2", consumerGroup = "my-consumer_test-topic-2")
	public class MyConsumer2 implements RocketMQListener<OrderPaidEvent> {
		public void onMessage(OrderPaidEvent orderPaidEvent) {
			log.info("received orderPaidEvent from topic test-topic-2: {}", orderPaidEvent);
		}
	}

	@Service
	@RocketMQMessageListener(topic = "test-topic-tag-test", consumerGroup = "test-topic-tag-test")
	public class MyConsumer3 implements RocketMQListener<String> {
		public void onMessage(String message) {
			log.info("received message from topic test-topic-tag-test: {}", message);
		}
	}
}

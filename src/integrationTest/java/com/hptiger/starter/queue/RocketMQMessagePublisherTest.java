package com.hptiger.starter.queue;

import java.math.BigDecimal;


import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hptiger.starter.queue.msg.OrderPaidEvent;
import com.hptiger.starter.queue.pub.RocketMQMessagePublisher;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class RocketMQMessagePublisherTest {
	

	@Autowired
	@Qualifier("rocketmq")
	private RocketMQMessagePublisher publisher;

	@Test
	public void testPublish() {
		publisher.publish("test-topic-1", "Test message1 from topic 1");
		publisher.publish("test-topic-1", "Test message2 from topic 1");
		publisher.publish("test-topic-2", new OrderPaidEvent("T_002", new BigDecimal("130.00")));
		publisher.publish("Hello World, I'm here...");
		publisher.publish("test-topic-tag-test:tag11", MessageBuilder
				.withPayload("Hello, World! I'm from spring message from topic test-topic-tag-test").build());

	}
}

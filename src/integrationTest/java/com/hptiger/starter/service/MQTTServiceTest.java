/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.service;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hptiger.starter.mqtt.MqttHandler;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class MQTTServiceTest {
	@Autowired
	MqttHandler handler;
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void teardown() {
		this.handler.close();
	}

	@Test
	public void testUserNameAndPasswordConnection() throws Exception {		
		handler.setMqttUser("Me");
		handler.setMqttPassword("1234567");
		handler.setClientId("hello-client");
		handler.subscribeTopic("hello-world", new IMqttMessageListener() {
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				Assert.assertEquals("topic","hello-world");
				Assert.assertEquals("hello-c",new String(message.getPayload()));				
			}			
		});
		Thread.sleep(5000L);
		handler.publishMessage("hello-world", "hello-c");		
	}
}

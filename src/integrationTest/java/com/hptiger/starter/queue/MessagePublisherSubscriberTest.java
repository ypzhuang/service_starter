/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.queue;

import java.util.HashMap;
import java.util.Map;

import com.hptiger.starter.utils.NumberHelper;
import com.hptiger.starter.context.SpringContextBridge;
import com.hptiger.starter.queue.msg.ValidationCodeMessage;
import com.hptiger.starter.service.impl.aliyun.SMSService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class MessagePublisherSubscriberTest {
	@Autowired
	MessagePublisher redisPublisher;
	
	@Test
	public void publish_whenInvokeWithValidationCodeMessage_ThenValidateCodeAsTrue() {		
		ValidationCodeMessage validationCodeMessage = new ValidationCodeMessage();
    	validationCodeMessage.setFromTenentCode("ORG10001");
    	validationCodeMessage.setTo("13816991878");
    	validationCodeMessage.setTemplate("login");
    	String code = NumberHelper.randomCode();
		Map<String,String> params = new HashMap<String,String>();
		params.put("code", code);
    	validationCodeMessage.setParams(params);
    	redisPublisher.publish(validationCodeMessage);
    	
    	try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
    	
    	SMSService smsService = SpringContextBridge.services().getService("ORG10001", SMSService.class);
    	Assert.assertTrue(smsService.validateCode("13816991878",code));
	}
}

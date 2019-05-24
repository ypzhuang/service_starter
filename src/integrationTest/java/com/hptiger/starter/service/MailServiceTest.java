/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.service;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import com.google.common.collect.ImmutableMap;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class MailServiceTest {
	@Autowired
	private IMailService mailService;
	
	@Test
	public void sendSimpleMessage_() {
		mailService.sendSimpleMessage("johnzhuang@mailinator.com", "Hello Simple Messge", "This is the body.");
	}
	
	@Test
	public void sendMessageWithAttachment_() {
		mailService.sendMessageWithAttachment("johnzhuang@mailinator.com", "Hello Simple Messge With attachment", "This is the body.","/Users/ypzhuang/Desktop/building2.png");
	}
	
	@Test
	public void sendTemplateMessage_() {
		Map<String,String> map = ImmutableMap.of("code", "654321");
		mailService.sendTemplateMessage("johnzhuang@mailinator.com", "Hello  Template Message", "login",map);
	}	
}

/**
 * created Feb 26, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hp.tiger.starter.service;

import org.springframework.scheduling.annotation.Async;

public interface IMailService {

	@Async
	void sendSimpleMessage(String to, String subject, String text);

	@Async
	void sendMessageWithAttachment(String to, String subject, String text, String ...pathToAttachment);
	
	@Async
	void sendTemplateMessage(String to, String subject, String template, Object model, String ...pathToAttachments);
}

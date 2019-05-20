/**
 * created Feb 26, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.hptiger.starter.service.IMailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public  class MailService implements IMailService {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JavaMailSenderImpl emailSender;
	
	@Autowired
	private Configuration freemarkerConfig;

	@Override
	public void sendSimpleMessage(String to, String subject, String text) {
		log.debug("Try to set email to {} with subject {} with body {} ", to, subject, text);
		SimpleMailMessage message = new SimpleMailMessage();		
		message.setFrom(getFrom());
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);		
	}

	private String getFrom() {
		Object nickName = emailSender.getJavaMailProperties().get("nickName");
		try {
			InternetAddress from = new InternetAddress(emailSender.getUsername(), nickName.toString());
			return from.toString();
		} catch (UnsupportedEncodingException e) {		
			e.printStackTrace();
		}
		return emailSender.getUsername();
	}
	@Override
	public void sendMessageWithAttachment(String to, String subject, String text, String ...pathToAttachments) {
		log.debug("Try to set email to {} with subject {} with body {} ", to, subject, text);
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			message.setFrom(getFrom());
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);
			for(String pathToAttachment : pathToAttachments) {
				FileSystemResource file = new FileSystemResource(new File(pathToAttachment));				
				helper.addAttachment(file.getFilename(), file);
			}			
			emailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void sendTemplateMessage(String to, String subject, String template, Object model, String ...pathToAttachments) {
		log.debug("Try to set email to {} with subject {} using template {} ", to, subject, template);
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(getFrom());
			helper.setTo(to);
			helper.setSubject(subject);
			
			Template t = freemarkerConfig.getTemplate(template + ".ftl");
			
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(t,model);
			helper.setText(text, true);
			
			for(String pathToAttachment : pathToAttachments) {
				FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
				helper.addAttachment(file.getFilename(), file);
			}			
			emailSender.send(message);
		} catch (MessagingException  e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (TemplateException e) {			
			e.printStackTrace();
		} 		
	}
}

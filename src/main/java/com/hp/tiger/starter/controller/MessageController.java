package com.hp.tiger.starter.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/app/v1/messages")
public class MessageController extends BaseController {
	
	@PostMapping(value = "/email")
    @ApiOperation(value = "发送邮件")
	public String sendEmail() {
		return UUID.randomUUID().toString();
	}
	
	@PostMapping(value = "/sms")
    @ApiOperation(value = "发送短信")
	public String sendSMS() {
		return UUID.randomUUID().toString();
	}
	
	@PutMapping(value = "/email/{messageId}")
    @ApiOperation(value = "取消邮件发送")
	public void cancelEmail(@PathVariable String messageId) {		
	}
	
	@PutMapping(value = "/sms/{messageId}")
    @ApiOperation(value = "取消短信发送")
	public void cancelSMS(@PathVariable String messageId) {		
	}
	
	@GetMapping(value = "/email/{messageId}")
    @ApiOperation(value = "获取邮件发送状态")
	public String getEmailStatus(@PathVariable String messageId) {	
		return "Sending";
	}
	
	@GetMapping(value = "/sms/{messageId}")
    @ApiOperation(value = "获取短信发送状态")
	public String getSMSStatus(@PathVariable String messageId) {
		return "Sent";
	}	
}

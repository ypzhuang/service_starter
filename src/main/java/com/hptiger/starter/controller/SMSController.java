/**
 * created Feb 25, 2019 by ypzhuang
 * 
 * 短信验证码接口
 */

package com.hptiger.starter.controller;

import java.util.HashMap;
import java.util.Map;

import com.hptiger.starter.context.SpringContextBridge;
import com.hptiger.starter.service.impl.aliyun.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hptiger.starter.ex.ApplicationException;
import com.hptiger.starter.queue.MessagePublisher;
import com.hptiger.starter.queue.ValidationCodeMessage;
import com.hptiger.starter.utils.NumberHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/sms")
@Api(tags  = {"SMS"})
public class SMSController extends BaseController {
	
	@Autowired
	MessagePublisher redisPublisher;
	
	@GetMapping("/login")
	@ApiOperation(value = "发送登录验证码短信")
	public void sendLoginCode(
			@ApiParam(value = "手机号", required = true, defaultValue = "17301771677") @RequestParam(required = true, defaultValue = "17301771677") String phone) {
		ValidationCodeMessage validationCodeMessage = new ValidationCodeMessage();
		validationCodeMessage.setFromTenentCode("ORG10001");
		validationCodeMessage.setTemplate("login");
		validationCodeMessage.setTo(phone);
		Map<String,String> model = new HashMap<String,String>();
		model.put("code", NumberHelper.randomCode());
		validationCodeMessage.setParams(model);
		redisPublisher.publish(validationCodeMessage);		
	}
	
	@GetMapping("/verfiy")
	@ApiOperation(value = "发送手机验证码短信")
	public void sendVerfiyCode(
			@ApiParam(value = "手机号", required = true, defaultValue = "17301771677") @RequestParam(required = true, defaultValue = "17301771677") String phone) {
		ValidationCodeMessage validationCodeMessage = new ValidationCodeMessage();
		validationCodeMessage.setFromTenentCode("ORG10001");
		validationCodeMessage.setTemplate("check");
		validationCodeMessage.setTo(phone);
		Map<String,String> model = new HashMap<String,String>();
		model.put("code", NumberHelper.randomCode());
		validationCodeMessage.setParams(model);
		redisPublisher.publish(validationCodeMessage);	
	}
	
	@PostMapping("/validate")
	@ApiOperation(value = "验证短信验证码")
	public void validateCode(
			@ApiParam(value = "手机号", required = true, defaultValue = "17301771677") @RequestParam(required = true, defaultValue = "17301771677") String phone,
			@ApiParam(value = "验证码", required = true) @RequestParam(required = true) String code
			) {
		SMSService smsService = SpringContextBridge.services().getService("ORG10001", SMSService.class);
		if(!smsService.validateCode(phone,code)) {
			throw new ApplicationException("验证码错误!");
		}		
	}
}

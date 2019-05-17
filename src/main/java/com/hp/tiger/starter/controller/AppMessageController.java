package com.hp.tiger.starter.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.hp.tiger.starter.controller.BaseController;
import com.hp.tiger.starter.entity.AppMessage;
import com.hp.tiger.starter.service.IAppMessageService;
import com.hp.tiger.starter.vo.EmailMessageVO;
import com.hp.tiger.starter.vo.SMSMessageVO;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.Match;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * App Message Controller
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-16
 */
@RestController
@RequestMapping("/app/v1/messages")
@Api(tags = {"Message"})
public class AppMessageController extends BaseController {
	@Autowired
	private IAppMessageService messageService;
	
	@Autowired
	private HttpServletRequest request;
	
	@PostMapping(value = "/email")
    @ApiOperation(value = "发送邮件")
	public AppMessage sendEmail(@Valid @RequestBody EmailMessageVO messageVO) {
		String appId = (String)request.getAttribute("APP_ID");
		AppMessage message = messageService.saveEmailMessage(appId, messageVO);
		messageService.publishMessage(message.getMessageId());
		return super.json.use(JsonView.with(message)
		        .onClass(AppMessage.class, Match.match().exclude("*").include("messageId"))
		       ).returnValue();
	}
	
	@PostMapping(value = "/sms")
    @ApiOperation(value = "发送短信")
	public AppMessage sendSMS(@Valid @RequestBody SMSMessageVO messageVO) {
		throw new RuntimeException("Not Implemented");
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

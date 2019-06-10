package com.hptiger.starter.controller;

import com.hptiger.starter.entity.enums.MessageClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hptiger.starter.entity.AppMessage;
import com.hptiger.starter.entity.enums.MessageStatus;
import com.hptiger.starter.service.IAppMessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;

/**
 * <p>
 * App Message Controller
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-16
 */
@RestController
@RequestMapping("/api/v1/messages")
@Api(tags = {"Message"})
public class MessageController extends BaseController {
	@Autowired
	private IAppMessageService messageService;
	

	@GetMapping
	@ApiOperation(value = "Search all the messages by page order by id desc")
	public IPage<AppMessage> getOrdersByPage(
			@ApiParam(value = "Message Id", required = false) @RequestParam(required = false) String  messageId,
			@ApiParam(value = "App Id", required = false) @RequestParam(required = false) String  appId,
			@ApiParam(value = "Message Class", required = false) @RequestParam(required = false) MessageClass messageClass,
			@ApiParam(value = "Message Status", required = false) @RequestParam(required = false) MessageStatus status,
			@ApiParam(value = "Receive Date From", required = false) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime receiveDateFrom,
			@ApiParam(value = "Receive Date To", required = false) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime receiveDateTo,
			@ApiParam(value = "Send Date From", required = false) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime sendDateFrom,
			@ApiParam(value = "Send Date To", required = false) @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime sendDateTo,
			@ApiParam(value = "当前页", required = true, defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
			@ApiParam(value = "每页数量", required = true, defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size) {
		return this.messageService.getMessagesByPage(messageId, appId, messageClass, status, receiveDateFrom,receiveDateTo,sendDateFrom , sendDateTo, current, size);
	}
}

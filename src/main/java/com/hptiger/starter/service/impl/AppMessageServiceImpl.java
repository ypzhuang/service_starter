package com.hptiger.starter.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hptiger.starter.adapter.LambdaQueryWrapperAdapter;
import com.hptiger.starter.entity.AppMessage;
import com.hptiger.starter.entity.enums.MessageClass;
import com.hptiger.starter.entity.enums.MessageStatus;
import com.hptiger.starter.entity.enums.MessageType;
import com.hptiger.starter.mapper.AppMessageMapper;
import com.hptiger.starter.service.IAppMessageService;
import com.hptiger.starter.utils.IDHelper;
import com.hptiger.starter.vo.EmailMessageVO;
import com.hptiger.starter.vo.SMSMessageVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-16
 */
@Service
public class AppMessageServiceImpl extends ServiceImpl<AppMessageMapper, AppMessage> implements IAppMessageService {
	private Gson gson = new Gson();
	
	@Transactional
	@Override
	public AppMessage saveEmailMessage(String appId, EmailMessageVO messageVO) {
		AppMessage message = new AppMessage();
		message.setAppId(appId);
		message.setContent(gson.toJson(messageVO));
		message.setCreatedBy(-1L);
		message.setMessageClass(MessageClass.EMAIL);
		message.setMessageType(MessageType.EMAIL);
		message.setMessageId(IDHelper.generateUUID());
		message.setStatus(MessageStatus.RECEIVED);
		message.setReceiveDate(LocalDateTime.now());
		this.save(message);
		this.publishMessage(message.getMessageId());
		return message;
	}

	@Transactional
	@Override
	public AppMessage saveSMSMessage(String appId, SMSMessageVO messageVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void publishMessage(String messageId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPage<AppMessage> getMessagesByPage(String messageId, String appId, MessageClass messageClass, MessageStatus status, LocalDateTime receiveDateFrom, LocalDateTime receiveDateTo, LocalDateTime sendDateFrom, LocalDateTime sendDateTo, Integer current, Integer size) {
		Page<AppMessage> page = new Page<>(current,size);
		return this.page(page, new LambdaQueryWrapperAdapter<AppMessage>()
				.eq(AppMessage::getMessageId,messageId)
				.eq(AppMessage::getAppId, appId)
				.eq(AppMessage::getMessageClass, messageClass)
				.ge(AppMessage::getReceiveDate,receiveDateFrom)
				.le(AppMessage::getReceiveDate,receiveDateTo)
				.ge(AppMessage::getSendDate,sendDateFrom)
				.le(AppMessage::getSendDate,sendDateTo)
		);
	};

}

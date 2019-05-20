package com.hptiger.starter.service.impl;

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
		return message;
	}

	@Override
	public AppMessage saveSMSMessage(String appId, SMSMessageVO messageVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void publishMessage(String messageId) {
		// TODO Auto-generated method stub
		
	}

}

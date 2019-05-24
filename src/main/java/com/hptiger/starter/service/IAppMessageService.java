package com.hptiger.starter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hptiger.starter.entity.AppMessage;
import com.hptiger.starter.entity.enums.MessageClass;
import com.hptiger.starter.entity.enums.MessageStatus;
import com.hptiger.starter.vo.EmailMessageVO;
import com.hptiger.starter.vo.SMSMessageVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-16
 */
public interface IAppMessageService extends IService<AppMessage> {
	AppMessage saveEmailMessage(String appId, EmailMessageVO messageVO);

	AppMessage saveSMSMessage(String appId, SMSMessageVO messageVO);

	void publishMessage(String messageId);

	IPage<AppMessage> getMessagesByPage(String messageId, String appId, MessageClass messageClass, MessageStatus status, LocalDateTime receiveDateFrom, LocalDateTime receiveDateTo, LocalDateTime sendDateFrom, LocalDateTime sendDateTo, Integer current, Integer size);
}

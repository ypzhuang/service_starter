package com.hp.tiger.starter.service;

import com.hp.tiger.starter.entity.AppMessage;
import com.hp.tiger.starter.vo.EmailMessageVO;
import com.hp.tiger.starter.vo.SMSMessageVO;
import com.baomidou.mybatisplus.extension.service.IService;

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
}

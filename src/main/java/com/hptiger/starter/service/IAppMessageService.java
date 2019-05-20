package com.hptiger.starter.service;

import com.hptiger.starter.entity.AppMessage;
import com.hptiger.starter.vo.EmailMessageVO;
import com.hptiger.starter.vo.SMSMessageVO;
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

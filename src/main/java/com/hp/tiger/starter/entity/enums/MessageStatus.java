package com.hp.tiger.starter.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageStatus implements IEnum<String> {
	RECEIVED("102001", "收到"), // received the message
	QUEUING("102002", "队列"), // published to MQ
	SENDING("102003", "发送"), // subscriber has got the message and and calling sender service to send
	SUCCESSED("102004", "成功"), // successfully sent the message
	CANCELED("102005", "取消"), // app has canceled the message sending request
	FAILED("102006", "失败"); // failed to send the message

	@EnumValue
	private final String value;
	private final String desc;

	MessageStatus(final String value, final String desc) {
		this.value = value;
		this.desc = desc;
	}

	@JsonValue
	@Override
	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}

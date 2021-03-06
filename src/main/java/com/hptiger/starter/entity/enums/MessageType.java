package com.hptiger.starter.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType implements IEnum<String> {
	SMS("102201", "短信"), 
	EMAIL("102202", "邮件");

	@EnumValue
	private final String value;
	private final String desc;

	MessageType(final String value, final String desc) {
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

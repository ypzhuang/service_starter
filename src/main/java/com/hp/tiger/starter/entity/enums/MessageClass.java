package com.hp.tiger.starter.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageClass implements IEnum<String> {
	SMS("102101", "短信"), 
	EMAIL("102102", "邮件");

	@EnumValue
	private final String value;
	private final String desc;

	MessageClass(final String value, final String desc) {
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

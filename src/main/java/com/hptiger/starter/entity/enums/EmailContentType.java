package com.hptiger.starter.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EmailContentType implements IEnum<String> {
	HTML("102301", "HTML"), 
	PLAIN("102302", "PLAIN");

	@EnumValue
	private final String value;
	private final String desc;

	EmailContentType(final String value, final String desc) {
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

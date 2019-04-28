/**
 * created Dec 31, 2018 by ypzhuang
 * 
 *  数据字典 是否删除
 */

package com.bdease.spm.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;


public enum Enable implements IEnum<String>{
	YES("100801", "启用"), NO("100802", "停用");

	@EnumValue
	private final String value;
	private final String desc;

	Enable(final String value, final String desc) {
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

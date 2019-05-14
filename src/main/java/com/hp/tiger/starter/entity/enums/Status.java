/**
 * created Dec 31, 2018 by ypzhuang
 * 
 *  数据字典 是否删除
 */

package com.hp.tiger.starter.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


public enum Status {
	YES("100000", "有效"), NO("100001", "无效");

	@EnumValue
	private final String value;
	private final String desc;

	Status(final String value, final String desc) {
		this.value = value;
		this.desc = desc;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}


	
}

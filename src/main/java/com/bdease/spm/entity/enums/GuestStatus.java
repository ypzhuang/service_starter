/**
 * created Dec 31, 2018 by ypzhuang
 * 
 *  数据字典 是否删除
 */

package com.bdease.spm.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GuestStatus implements IEnum<String> {
	NORMAL("104001", "正常"),
	ABNORML("104002", "异常");

	private final String value;
	private final String desc;

	GuestStatus(final String value, final String desc) {
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
	
	public static GuestStatus getEnum(String value) {
		for(GuestStatus status : GuestStatus.values()) {
			if(status.getValue().equalsIgnoreCase(value.trim())) {
				return status;
			}
		}
		return null;
	}
}

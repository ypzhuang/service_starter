/**
 * created Dec 31, 2018 by ypzhuang
 * 
 *  数据字典 是否删除
 */

package com.bdease.spm.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GoodsStatus implements IEnum<String> {
	FOR_SALE("103001", "在售"),
	OFFLINE("103002", "暂售");

	private final String value;
	private final String desc;

	GoodsStatus(final String value, final String desc) {
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
	
	public static GoodsStatus getEnum(String value) {
		for(GoodsStatus status : GoodsStatus.values()) {
			if(status.getValue().equalsIgnoreCase(value.trim())) {
				return status;
			}
		}
		return null;
	}
}

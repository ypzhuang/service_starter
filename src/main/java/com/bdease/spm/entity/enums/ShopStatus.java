/**
 * created Dec 31, 2018 by ypzhuang
 * 
 *  数据字典 是否删除
 */

package com.bdease.spm.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ShopStatus implements IEnum<String> {
	OPEN("102001", "营业"), 
	CLOSE("102002", "歇业");

	private final String value;
	private final String desc;

	ShopStatus(final String value, final String desc) {
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
	
	public static ShopStatus getEnum(String value) {
		for(ShopStatus status : ShopStatus.values()) {
			if(status.getValue().equalsIgnoreCase(value.trim())) {
				return status;
			}
		}
		return null;
	}
}

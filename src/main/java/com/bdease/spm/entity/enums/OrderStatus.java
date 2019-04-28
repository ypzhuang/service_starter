/**
 * created Dec 31, 2018 by ypzhuang
 * 
 *  数据字典 是否删除
 */

package com.bdease.spm.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus implements IEnum<String> {
	NEW("105001", "已新建"),
	TO_PAY("105002", "待支付"),
	PAYED("105003", "已支付"),
	CANCELD("105004", "已取消");


	private final String value;
	private final String desc;

	OrderStatus(final String value, final String desc) {
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
	
	public static OrderStatus getEnum(String value) {
		for(OrderStatus status : OrderStatus.values()) {
			if(status.getValue().equalsIgnoreCase(value.trim())) {
				return status;
			}
		}
		return null;
	}
}

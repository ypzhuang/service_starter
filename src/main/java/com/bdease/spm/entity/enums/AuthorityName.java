package com.bdease.spm.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AuthorityName implements IEnum<String>{
    ROLE_SHOP_USER("ROLE_SHOP_USER","店员"), 
    ROLE_SHOP_ADMIN("ROLE_SHOP_ADMIN","店长"),
    ROLE_MANAGER("ROLE_MANAGER","管理员"),//合伙人
    ROLE_GUEST("ROLE_GUEST","客人"),
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN","超级管理员");
  
    
    @EnumValue
    private final String value;
    private final String desc;

    AuthorityName(final String value, final String desc) {
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
    public static AuthorityName getEnum(String value) {
		for(AuthorityName authorityName : AuthorityName.values()) {
			if(authorityName.getValue().equalsIgnoreCase(value.trim())) {
				return authorityName;
			}
		}
		return null;
	}
}
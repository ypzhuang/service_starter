package com.hp.tiger.starter.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DelFlag implements IEnum<String>{
    YES("0", "有效"),
    NO("1", "删除");

    @EnumValue
    private final String value;
    private final String desc;

    DelFlag(final String value, final String desc) {
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

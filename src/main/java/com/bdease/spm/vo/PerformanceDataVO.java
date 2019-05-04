package com.bdease.spm.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PerformanceDataVO {
	
	@ApiModelProperty(value = "销售人员")
	private String soldName;
	
	@ApiModelProperty(value = "销售人员Id")
    private Long soldBy;
	
	@ApiModelProperty(value = "当前月1号到当前日，每日销售金额")
    private List<BigDecimal> data;
}

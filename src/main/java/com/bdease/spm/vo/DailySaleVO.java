package com.bdease.spm.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DailySaleVO对象", description = "绩效")
public class DailySaleVO {
	@ApiModelProperty(value = "金额")
	private BigDecimal amount;

	@ApiModelProperty(value = "订单日期")
	@JsonFormat(pattern = "dd")
	private LocalDate orderDate;
}

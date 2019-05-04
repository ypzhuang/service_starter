package com.bdease.spm.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SaleRecordVO对象", description = "销售记录")
public class SaleRecordVO {
	@ApiModelProperty(value ="客户微信头像")
	private String avatarUrl;
	
	@ApiModelProperty(value ="客户昵称")
	private String nickName;
	
	@ApiModelProperty(value ="客户姓名")
	private String name;
	
	@ApiModelProperty(value ="订单日期")
	@JsonFormat(pattern = "MM/dd")
	private LocalDate orderDate;
	
	@ApiModelProperty(value ="订单支付金额")
	private BigDecimal payAmount;
	
	@ApiModelProperty(value ="销售人员姓名")
	private String soldByName; 
}

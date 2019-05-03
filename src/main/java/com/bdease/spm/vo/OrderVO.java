package com.bdease.spm.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单VO
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Data
@ApiModel(value = "OrderVO对象", description = "订单")
public class OrderVO {
	@ApiModelProperty(value = "客户ID")
	private Long miniUserId;
	

	@ApiModelProperty(value = "优惠金额")
	private BigDecimal discountAmount = new BigDecimal(0.0);

	@ApiModelProperty(value = "实付金额")
	private BigDecimal payAmount = new BigDecimal(0.0);

	@ApiModelProperty(value = "销售店铺ID")
	private Long shopId;

	@ApiModelProperty(value = "销售人员ID")
	private Long soldBy;

	@ApiModelProperty(value = "订单明细")
	private List<OrderItemVO> orderItems;
}

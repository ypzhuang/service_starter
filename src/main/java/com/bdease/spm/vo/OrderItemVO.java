package com.bdease.spm.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单项目
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Data
@ApiModel(value = "OrderItemVO对象", description = "订单项目")
public class OrderItemVO {

	@ApiModelProperty(value = "商品ID")
	@NotNull(message = "商品ID不能为空")
	private Long goodsId;	
	
	@ApiModelProperty(value = "销售价格")
	@NotNull(message = "销售价格不能为空")
	@PositiveOrZero(message = "销售价格>=0" )
	private BigDecimal price;

	
	@ApiModelProperty(value = "数量")
	@NotNull(message = "销售数量不能为空")
	@Positive(message = "销售数量>0" )
	private Integer quantity;
}

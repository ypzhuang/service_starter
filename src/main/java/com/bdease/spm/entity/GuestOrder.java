package com.bdease.spm.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bdease.spm.entity.BaseEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bdease.spm.entity.enums.DelFlag;
import com.bdease.spm.entity.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Order对象", description="订单")
public class GuestOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag = DelFlag.YES;

    @ApiModelProperty(value = "客户ID")
    private Long miniUserId;

    @ApiModelProperty(value = "订单日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsQuantity;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "状态")
    private OrderStatus status = OrderStatus.PAYED;

    @ApiModelProperty(value = "销售店铺")
    private Long shopId;

    @ApiModelProperty(value = "销售人员")
    private Long soldBy;

    @ApiModelProperty(value = "销售店铺名称")
    private String shopName;

    @ApiModelProperty(value = "销售人员名称")
    private String soldByName;

    @ApiModelProperty(value = "订单明细")
    private transient List<OrderItem> items = new ArrayList<>();
}

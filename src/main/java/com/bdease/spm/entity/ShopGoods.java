package com.bdease.spm.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bdease.spm.entity.BaseEntity;
import com.bdease.spm.entity.enums.DelFlag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="ShopGoods对象", description="店铺在售商品")
public class ShopGoods extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag = DelFlag.YES;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "店铺价格")
    private BigDecimal price;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
}

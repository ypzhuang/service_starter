package com.bdease.spm.vo;

import com.bdease.spm.converter.PicturePathConverter;
import com.bdease.spm.entity.enums.GoodsStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Data
@ApiModel(value="ShopGoodsVO对象", description="店铺在售商品")
public class ShopGoodsVO {
    @ApiModelProperty(value = "ShopGoods ID")
    private Long id;

    @ApiModelProperty(value = "商品照片URL")
    private String imgUrl;

    @ApiModelProperty(value = "商品编号")

    private String identifier;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品规格")
    private String spec;

    @ApiModelProperty(value = "统一价格")
    private BigDecimal price;

    @ApiModelProperty(value = "店铺价格")
    private BigDecimal shopPrice;

    @ApiModelProperty(value = "状态")
    private GoodsStatus status;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    public String getPicturePath() {
        return PicturePathConverter.getInstance().picturePathWithFileServer(this.imgUrl);
    }
}

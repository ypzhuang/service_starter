package com.bdease.spm.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bdease.spm.converter.PicturePathConverter;
import com.bdease.spm.entity.BaseEntity;
import com.bdease.spm.entity.enums.DelFlag;
import com.bdease.spm.entity.enums.GoodsStatus;
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
@ApiModel(value="Goods对象", description="商品")
public class Goods extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag = DelFlag.YES;

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

    @ApiModelProperty(value = "状态")
    private GoodsStatus status = GoodsStatus.FOR_SALE;

    @ApiModelProperty(value = "商品描述")
    private String description;

    public String getImgUrl() {
        return PicturePathConverter.getInstance().picturePathWithFileServer(this.imgUrl);
    }
}

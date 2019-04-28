package com.bdease.spm.vo;

import com.bdease.spm.entity.enums.GoodsStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@ApiModel(value="GoodsVO对象", description="商品")
public class GoodsVO {
    @ApiModelProperty(value = "商品照片URL")
    private String imgUrl;

    @ApiModelProperty(value = "商品编号")
    @NotNull(message = "商品编号不能为空")
    @Size(min = 8, max = 8 ,message = "商品编号长度为8位字符")
    private String identifier;

    @ApiModelProperty(value = "商品名称")
    @NotNull(message = "商品名称不能为空")
    @Size(min = 2, max = 50 ,message = "商品名称长度2-50位字符")
    private String name;

    @ApiModelProperty(value = "商品规格")
    @NotNull(message = "商品规格不能为空")
    @Size(min = 2, max =10 ,message = "商品规格长度2-10位字符")
    private String spec;

    @ApiModelProperty(value = "统一价格")
    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;

    @ApiModelProperty(value = "状态")
    @NotNull(message = "商品状态不能为空")
    private GoodsStatus status;

    @ApiModelProperty(value = "商品描述")
    @Size(min = 0, max =10 ,message = "商品描述最多500位字符")
    private String description;
}

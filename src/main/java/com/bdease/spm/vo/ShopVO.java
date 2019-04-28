package com.bdease.spm.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bdease.spm.entity.enums.ShopStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * <p>
 * 店铺信息
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-14
 */
@Data
@ApiModel(value="ShopVO对象", description="店铺")
public class ShopVO {

    @ApiModelProperty(value = "省份代码")
    @NotNull(message = "省份代码不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "城市代码")
    @NotNull(message = "城市代码不能为空")
    private String cityCode;

    @ApiModelProperty(value = "店铺名称")
    @NotNull(message = "店铺名称不能为空")
    private String name;

    @ApiModelProperty(value = "店铺地址")
    @Size(min = 10, max = 500 ,message = "店铺地址至少10个字符,最多500个字符")
    private String address;

    @ApiModelProperty(value = "营业状态")
    @NotNull(message = "营业状态不能为空")
    private ShopStatus status;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "店员列表")
    private List<Long> shopUsers = new ArrayList<>();

    @ApiModelProperty(value = "店长")
    @Min(value = 1,message = "店长Id> 0")
    private Long shopAdmin;

    @ApiModelProperty(value = "管理员")
    @Min(value = 1,message = "管理员Id> 0")
    private Long shopManager;

}

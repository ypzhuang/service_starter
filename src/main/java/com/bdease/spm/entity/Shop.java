package com.bdease.spm.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bdease.spm.entity.BaseEntity;
import com.bdease.spm.entity.enums.DelFlag;
import com.bdease.spm.entity.enums.ShopStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺信息
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Shop对象", description="店铺信息")
public class Shop extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag = DelFlag.YES;

    @ApiModelProperty(value = "省份代码")
    private String provinceCode;

    @ApiModelProperty(value = "城市代码")
    private String cityCode;

    @ApiModelProperty(value = "店铺名称")
    private String name;

    @ApiModelProperty(value = "店铺地址")
    private String address;

    @ApiModelProperty(value = "营业状态")
    private ShopStatus status;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;


    @ApiModelProperty(value = "店员列表")
    private transient List<Long> shopUsers = new ArrayList<>();

    @ApiModelProperty(value = "店长")
    private transient Long shopAdmin;

    @ApiModelProperty(value = "管理员")
    private transient Long shopManager;
}

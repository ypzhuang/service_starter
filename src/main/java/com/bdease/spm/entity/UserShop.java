package com.bdease.spm.entity;

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
 * 
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="UserShop对象", description="")
public class UserShop extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag = DelFlag.YES;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;


}

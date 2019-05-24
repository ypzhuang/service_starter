package com.hptiger.starter.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hptiger.starter.entity.enums.DelFlag;

import com.hptiger.starter.entity.enums.Enable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * Third Part APP Management
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="App对象", description="Third Part APP Management")
public class App extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag;

    @ApiModelProperty(value = "App ID")
    private String appId;

    @ApiModelProperty(value = "App Security")
    private String appSecurity;

    @ApiModelProperty(value = "Owner Email")
    private String ownerEmail;

    @ApiModelProperty(value = "状态")
    private Enable status = Enable.YES;
}

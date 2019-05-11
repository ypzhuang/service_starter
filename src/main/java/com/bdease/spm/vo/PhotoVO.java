package com.bdease.spm.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 客户照片
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Data
@ApiModel(value="PhotoVO对象", description="客户照片")
public class PhotoVO {
    @ApiModelProperty(value = "客户ID")
    @Positive(message = "客户ID > 0")
    private Long miniUserId;

    @ApiModelProperty(value = "照片URL")
    @NotNull(message = "照片URL不能为空")
    @Size(min = 10, max = 200 ,message = "照片URL最长200位字符")
    private String photoUrl;
}

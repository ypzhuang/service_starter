package com.bdease.spm.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bdease.spm.converter.PicturePathConverter;
import com.bdease.spm.entity.BaseEntity;
import java.time.LocalDate;

import com.bdease.spm.entity.enums.DelFlag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户备注
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="Photo对象", description="客户备注")
public class Photo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    @JsonIgnore
    private DelFlag delFlag = DelFlag.YES;

    @ApiModelProperty(value = "客户ID")
    @JsonIgnore
    private Long miniUserId;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "照片URL")
    private String photoUrl;

    @ApiModelProperty(value = "拍照人ID")
    private Long takedBy;

    @ApiModelProperty(value = "拍照日期")
    private LocalDate takedDate;

    public String getPhotoUrl() {
        return PicturePathConverter.getInstance().picturePathWithFileServer(this.photoUrl);
    }
}

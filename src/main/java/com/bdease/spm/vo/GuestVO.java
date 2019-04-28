package com.bdease.spm.vo;

import com.bdease.spm.entity.enums.GuestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@ApiModel(value="GuestVO", description="客户")
public class GuestVO {

    @ApiModelProperty(value = "昵称")
    @Size(min = 0, max = 20 ,message = "昵称最长20位字符")
    private String nickName;

    @ApiModelProperty(value = "手机")
    @Size(min = 0, max = 20 ,message = "手机最长20位字符")
    private String phone;

    @ApiModelProperty(value = "店铺ID")
    @Positive(message = "店铺ID大于0")
    private Long shopId;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ApiModelProperty(value = "省份")
    @Size(min = 0, max = 20, message = "省份最长20位字符")
    private String province;

    @ApiModelProperty(value = "姓名")
    @Size(min = 1, max = 100, message = "姓名最长20位字符")
    private String name;

    @ApiModelProperty(value = "状态")
    private GuestStatus status;
}

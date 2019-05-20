package com.hptiger.starter.vo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.hptiger.starter.entity.enums.AuthorityName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
@Data
public class UserVO {

	@ApiModelProperty(value = "状态")
    private Boolean enabled;

    @ApiModelProperty(value = "姓名")
    @NotNull(message = "姓名不能为空")
    @Size(min = 2, max = 50 ,message = "姓名至少2个字符,最多50个字符")
    private String name;

    @ApiModelProperty(value = "手机号/用户名")
    @NotNull(message = "手机号/用户名不能为空")
    @Size(min = 2, max = 50 ,message = "姓名至少2个字符,最多50个字符")
    private String username;

    @ApiModelProperty(value = "入职日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate onboardDate;

    @ApiModelProperty(value = "职位")
    @NotNull(message = "职位不能为空")
    private AuthorityName role;
    
    @ApiModelProperty(value = "店铺")
    private List<Long> shops = new ArrayList<>();
}

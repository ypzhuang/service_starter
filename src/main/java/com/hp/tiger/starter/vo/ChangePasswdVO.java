package com.hp.tiger.starter.vo;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ChangePasswdVO对象", description = "密码修改")
public class ChangePasswdVO {
	@ApiModelProperty(value = "旧密码")
	@NotNull(message = "旧密码不能为空")
	private String oldPassword;
	
	@ApiModelProperty(value = "新密码")
	@NotNull(message = "新密码不能为空")
	@Size(min = 8, max=25,message = "新密码长度至少8位")
	private String newPassword;
}

package com.hp.tiger.starter.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AppVO", description = "App")
public class AppVO {
	@ApiModelProperty(value = "Owner Email")
	@Email
	@Size(min = 5, max = 50 ,message = "length of email should be less than 50")
	private String ownerEmail;
}

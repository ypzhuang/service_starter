/**
 * created Feb 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hp.tiger.starter.vo;

import javax.validation.constraints.NotNull;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="EncryptedMiniUserVO", description="加密的小程序用户信息")
public class EncryptedMiniUserVO {
	
	@ApiModelProperty(value = "encryptedData",required = true)
	@NotNull(message = "encryptedData不能为空")
	private String encryptedData;
	
	@ApiModelProperty(value = "sessionKey",required = false)	
	private String sessionKey;
	
	@ApiModelProperty(value = "iv",required = true)
	@NotNull(message = "iv不能为空")
	private String iv;
}

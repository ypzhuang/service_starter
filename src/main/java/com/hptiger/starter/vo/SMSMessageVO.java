package com.hptiger.starter.vo;

import java.util.Map;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Message
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-16
 */
@Data
@ApiModel(value="SMSMessageVO", description="短信消息")
public class SMSMessageVO {
	@ApiModelProperty(value = "Phone", required=true)
	private Phone phone;
	
	@ApiModelProperty(value = "SMS Template Code", required=true)
	private String templateCode;
	
	@ApiModelProperty(value = "Template Value of Object", required=false)
	private Map<String,String> templateValue;
	
	@Data
	@ApiModel(value="Phone", description="手机号")
	class Phone {
		@ApiModelProperty(value = "Country Codes", required=false, example="+86")
		private String countryCode = "+86";
		
		@ApiModelProperty(value = "Phone", required=true, example="13816991878")
		private String phone;
	}
}

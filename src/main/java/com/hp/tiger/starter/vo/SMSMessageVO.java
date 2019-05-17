package com.hp.tiger.starter.vo;

import java.util.Map;
import io.swagger.annotations.ApiModel;

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
	private Phone phone;
	private String templateCode;
	private Map<String,String> templateValue;
	
	@Data
	class Phone {
		private String to = "+86";
		private String phone;
	}
}

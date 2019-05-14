/**
 * created Feb 24, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hp.tiger.starter.config;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;


@ConfigurationProperties(prefix = "app.aliyun")
@Configuration
@Data
@NoArgsConstructor
public class AliyunConfiguration {
	private List<AliyunConfig>  aliyunConfigs;
	
	@Data
	@NoArgsConstructor
	public static class AliyunConfig {
		private String tenentCode;
		private String accessKeyId;
		private String accessSecret;
		private SmsConfig smsConfig;
		
		@Data
		@NoArgsConstructor
		public static class SmsConfig {
			private String signName;			
			List<SMSTemplate> templates;
			
			@Data
			@NoArgsConstructor
			public static class SMSTemplate {
				private String templateName;
				private String templateCode;
			}
		}
	}
	
	public AliyunConfig getAliyunConfig(String orgCode) {
		Optional<AliyunConfig> config = aliyunConfigs
				.parallelStream()
				.filter(smsConfig -> smsConfig.getTenentCode().equals(orgCode))
				.findFirst();
		if(config.isPresent()) return config.get();
		else throw new IllegalArgumentException("不存在的组织代码" + orgCode);		
	}
}

/**
 * created Feb 24, 2019 by ypzhuang
 * 
 * SMSService 阿里短信发送服务实现
 */

package com.bdease.spm.service.impl.aliyun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import com.bdease.spm.config.AliyunConfiguration.AliyunConfig.SmsConfig;
import com.bdease.spm.config.AliyunConfiguration.AliyunConfig.SmsConfig.SMSTemplate;
import com.bdease.spm.context.SpringContextBridge;
import com.bdease.spm.service.ISMSService;
import com.bdease.spm.utils.RedisHelper;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

public class SMSService extends AliyunService implements ISMSService {
	private Gson gson = new Gson();
	private SmsConfig smsConfig;

	@Data
	@NoArgsConstructor
	public static class SMSResponce {
		@SerializedName("Message")
		private String message;

		@SerializedName("RequestId")
		private String requestId;

		@SerializedName("Code")
		private String code;

		@SerializedName("BizId")
		private String bizId;
	}

	public SMSService(String tenentCode) {
		super(tenentCode);
		this.smsConfig = super.aliyunConfig.getSmsConfig();
	}

	@Override
	public boolean sendMessage(List<String> phoneNumbers, String template, Object model) {
		if (!CollectionUtils.isEmpty(phoneNumbers)) {
			String phones = String.join(",", phoneNumbers);
			sendMessage(phones, template, model);
		}
		return false;
	}

	@Override
	public boolean sendMessage(String phoneNumbers, String template, Object model) {
		SMSTemplate smsTemplate = this.smsConfig.getTemplates().stream()
				.filter(t -> t.getTemplateName().equalsIgnoreCase(template)).findFirst().get();
		
		if (!StringUtils.isAnyBlank(phoneNumbers)) {
			Map<String, String> dynaQueryParameters = new HashMap<String, String>();
			dynaQueryParameters.put("PhoneNumbers", phoneNumbers);
			dynaQueryParameters.put("TemplateCode", smsTemplate.getTemplateCode());
			dynaQueryParameters.put("TemplateParam", gson.toJson(model));

			String response = super.request(dynaQueryParameters);
			SMSResponce res = gson.fromJson(response, SMSResponce.class);
			if ("OK".equalsIgnoreCase(res.getCode())) {
				RedisHelper.storeCodeInRedis(phoneNumbers, this.getTenentCode(), model);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	String getDomain() {
		return "dysmsapi.aliyuncs.com";
	}

	@Override
	String getVersion() {
		return "2017-05-25";
	}

	@Override
	String getAction() {
		return "SendSms";
	}

	@Override
	Map<String, String> getStaticQueryParameters() {
		Map<String, String> queryParameters = new HashMap<String, String>();
		queryParameters.put("SignName", this.smsConfig.getSignName());
		return queryParameters;
	}

	@SuppressWarnings("unchecked")
	public boolean validateCode(String phone, String code) {		
		RedisTemplate<String,Object> redisTemplate = SpringContextBridge.services().getService("redisTemplate", RedisTemplate.class);		
		String key = String.join(":", this.getTenentCode(), phone);
		String storedCode = (String)redisTemplate.opsForValue().get(key);	
		boolean flag =  code.equals(storedCode);
		if(flag) {
			redisTemplate.opsForValue().getOperations().delete(key);
		}
		return flag;	
	}
}

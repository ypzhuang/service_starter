/**
 * created Feb 24, 2019 by ypzhuang
 * 
 * AliyunService 通用阿里服务调用
 */

package com.hptiger.starter.service.impl.aliyun;

import java.util.Map;

import com.hptiger.starter.config.AliyunConfiguration;
import com.hptiger.starter.context.SpringContextBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;

public abstract class AliyunService {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected AliyunConfiguration.AliyunConfig aliyunConfig;

	private IAcsClient client = null;

	private String tenentCode;
	
	public AliyunService(String tenentCode) {
		this.tenentCode = tenentCode;
		this.aliyunConfig = SpringContextBridge.services().getService(AliyunConfiguration.class)
				.getAliyunConfig(tenentCode);
		DefaultProfile profile = DefaultProfile.getProfile(getRegionId(), getAccessKey(), getAccessSecret());
		client = new DefaultAcsClient(profile);
	}

	public String request(Map<String, String> dynaQueryParameters) {
		CommonRequest request = new CommonRequest();
		request.setProtocol(getProtocolType());
		request.setMethod(getMethodType());
		request.setDomain(getDomain());
		request.setVersion(getVersion());
		request.setAction(getAction());

		Map<String, String> queryParameters = getStaticQueryParameters();
		for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
			request.putQueryParameter(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<String, String> entry : dynaQueryParameters.entrySet()) {
			request.putQueryParameter(entry.getKey(), entry.getValue());
		}

		try {
			CommonResponse response = client.getCommonResponse(request);
			String data = response.getData();			
			log.debug("Aliyun Responce:{}", data);			
			return data;
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String getRegionId() {
		return "default";
	}

	protected String getAccessKey() {
		return this.aliyunConfig.getAccessKeyId();
	}

	protected String getAccessSecret() {
		return this.aliyunConfig.getAccessSecret();
	}

	protected ProtocolType getProtocolType() {
		return ProtocolType.HTTPS;
	}

	protected MethodType getMethodType() {
		return MethodType.POST;
	}

	abstract String getDomain();

	abstract String getVersion();

	abstract String getAction();

	abstract Map<String, String> getStaticQueryParameters();
	
	public String getTenentCode() {
		return this.tenentCode;
	}
}

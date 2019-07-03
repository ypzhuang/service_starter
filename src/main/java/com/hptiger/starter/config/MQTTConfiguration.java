/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.hptiger.starter.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "app.mqtt")
@Configuration
@Data
@NoArgsConstructor
public class MQTTConfiguration {
	String mqttUser;
	String mqttPassword;
	public String certPath;
	public String caFilePath;
	public String clientCrtFilePath;
	public String clientKeyFilePath;
	public String clientCrtFilePasswod ;
	public String clientId;
	public Integer connectionTimeoutInSeconds;
	public Integer keepAliveIntervalInSeconds;
	public Boolean cleanSession;
	List<String> mqttServers;
}

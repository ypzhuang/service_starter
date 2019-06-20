/**
 * created Dec 28, 2018 by ypzhuang
 * 
 * Swagger Config
 */

package com.hptiger.starter.config;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.hptiger.starter.utils.FileHelper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({"dev","test"})
public class SwaggerConfig {
	@Value("${spring.application.name}")
	private String applicationName;
	

	private String apiChangeHistory(String fileName) {	
		InputStream input;
		try {
			input = FileHelper.getInputStream(fileName);
			return IOUtils.toString(input, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Bean
	public Docket appApi() {	
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("APP Service")
				.apiInfo(appInfo())
				.select()
				.paths(PathSelectors.ant("/app/**")).build()
				.securitySchemes(newArrayList(apiAppId(), apiAppSecurity()))				
				.tags(
						new Tag("AppMessages","App消息")
				);
	}	

	@Value("${app_api.header.appId}")
	private String appIdHeader;
	
	@Value("${app_api.header.appSecurity}")
	private String appSecurityHeader;
	
	@Bean
	SecurityScheme apiAppId() {
		return new ApiKey("appId", appIdHeader, "header");
	}

	@Bean
	SecurityScheme apiAppSecurity() {
		return new ApiKey("appSecurity", appSecurityHeader, "header");
	}
	
	private ApiInfo appInfo() {		
		return new ApiInfoBuilder()
				.title(applicationName + " APP API Online")
				.description(apiChangeHistory("service_api_changehistory.md"))
				.contact(new Contact("John Zhuang", "", "yinping.zhuang@hp.com"))
				.version("1.0.0-SNAPSHOT")
				.build();
	}
}

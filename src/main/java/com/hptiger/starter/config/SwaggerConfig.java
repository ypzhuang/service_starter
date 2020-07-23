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
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("API Service")
				.apiInfo(apiInfo())
				.select()
				.paths(PathSelectors.ant("/api/**"))
				.build()
				.securitySchemes(newArrayList(apiKey()))
				.tags(					
						new Tag("Home","Home"),	
						new Tag("Auth","认证"),
						new Tag("Dict","数据字典")
				);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(applicationName + " Service API Online")
				.description(apiChangeHistory("app_api_changehistory.md"))
				.contact(new Contact("John Zhuang", "", "zhuangyinping@gmail.com"))
				.version("1.0.0-SNAPSHOT")
				.build();
	}

	@Bean
    SecurityScheme apiKey() {
		return new ApiKey("token", "Authorization", "header");
	}
	
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
}

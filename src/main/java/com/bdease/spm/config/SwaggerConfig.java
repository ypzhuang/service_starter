/**
 * created Dec 28, 2018 by ypzhuang
 * 
 * Swagger Config
 */

package com.bdease.spm.config;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.bdease.spm.utils.FileHelper;
import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
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
				.groupName("Service")
				.apiInfo(apiInfo())
				.select()
				.paths(businessOnlyEndpoints())
				.build()
				.securitySchemes(newArrayList(apiKey()))
				.tags(
						new Tag("Order", "订单管理,消费列表，销售记录"),
						new Tag("Employee", "员工管理"),
						new Tag("File","文件服务"),
						new Tag("Goods","商品管理"),
						new Tag("Guest","客户管理"),
						new Tag("Home","Home"),
						new Tag("Photo","护理照片"),
						new Tag("Shop","店铺"),
						new Tag("ShopGoods","店铺在售商品"),
						new Tag("SMS","短信服务"),
						new Tag("Auth","认证"),
						new Tag("Dict","数据字典")

				);
	}

	private Predicate<String> businessOnlyEndpoints() {
		return new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return !input.contains("error");
			}
		};
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(applicationName + "微服务Online  API文档")
				.description(apiChangeHistory())
				.contact(new Contact("John Zhuang", "", "zhuangyinping@gmail.com"))
				.version("0.0.1-SNAPSHOT")
				.build();
	}

	@Bean
    SecurityScheme apiKey() {
		return new ApiKey("token", "Authorization", "header");
	}
	
	private String apiChangeHistory() {	
		InputStream input;
		try {
			input = FileHelper.getInputStream("changehistory.md");
			return IOUtils.toString(input, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}	
}

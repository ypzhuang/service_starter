/**
 * created Jan 6, 2019 by ypzhuang
 * 
 * 解决枚举类型值作为参数不能正确转化成枚举类型通用解决方案;API性能统计
 */

package com.hptiger.starter.config;


import com.hptiger.starter.config.interceptor.SpringPerformanceInterceptor;
import com.hptiger.starter.converter.UniversalEnumConverterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	@Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new UniversalEnumConverterFactory());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(springPerformanceInterceptor());		
	}
	
	@Bean
	public SpringPerformanceInterceptor springPerformanceInterceptor() {
		return new SpringPerformanceInterceptor();
	}
}


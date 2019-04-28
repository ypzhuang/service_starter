/**
 * created Mar 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.bdease.spm.utils;

import java.time.Duration;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;

import com.bdease.spm.context.SpringContextBridge;

public class RedisHelper {
	public static void storeCodeInRedis(String phoneNumbers, String tenentCode, Object model) {		
		if(model instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String,String> map = (Map<String,String>)model;
			if(map.containsKey("code")) {
				String code = map.get("code");
				String []phones = phoneNumbers.split(",");
				for(String phone:phones) {
					storeCodeInRedis(phone, tenentCode, code);
				}				
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void storeCodeInRedis(String phone, String tenentCode, String code) {		
		RedisTemplate<String,Object> redisTemplate = SpringContextBridge.services().getService("redisTemplate", RedisTemplate.class);
		redisTemplate.opsForValue().set(String.join(":", tenentCode, phone), code, Duration.ofSeconds(600));
	}
}

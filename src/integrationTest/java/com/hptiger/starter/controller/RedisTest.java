/**
 * created Dec 27, 2018 by ypzhuang
 * 
 * 测试 AuthController
 */

package com.hptiger.starter.controller;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class RedisTest {	
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	
    Gson gson = new Gson();
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Test
    public void testStringTemplate() {
        stringRedisTemplate.opsForValue().set("Name", "Susan King");
        stringRedisTemplate.opsForValue().set("Age", "29");
        Assert.assertEquals("Susan King", stringRedisTemplate.opsForValue().get("Name"));
        Assert.assertEquals("29", stringRedisTemplate.opsForValue().get("Age"));
    }
}

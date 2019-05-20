/**
 * created Dec 27, 2018 by ypzhuang
 * 
 * 测试 AuthController
 */

package com.hptiger.starter.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hptiger.starter.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
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
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void testStringTemplate() {
        stringRedisTemplate.opsForValue().set("Name", "Susan King");
        stringRedisTemplate.opsForValue().set("Age", "29");
        Assert.assertEquals("Susan King", stringRedisTemplate.opsForValue().get("Name"));
        Assert.assertEquals("29", stringRedisTemplate.opsForValue().get("Age"));
    }  
    
    @Test
    public void testStringTemplateForObject() {
    	User user = new User();
    	user.setName("BatMan");
    	user.setEnabled(true);
    	LocalDateTime lastLoginDate = LocalDateTime.now();
    	user.setLastLoginDate(lastLoginDate);
    	user.setUpdateTime(new Date());    	
    	stringRedisTemplate.opsForValue().set("BatMan", gson.toJson(user), Duration.ofSeconds(30));        
       
        User persistUser = gson.fromJson(stringRedisTemplate.opsForValue().get("BatMan"), User.class);
        Assert.assertEquals(user.getName(),persistUser.getName());
        Assert.assertTrue(persistUser.getEnabled());
        Assert.assertEquals(user.getLastLoginDate().getSecond(),persistUser.getLastLoginDate().getSecond());
        
        
        List<User> list = new ArrayList<>();
        user = new User();
        user.setName("IronMan");
        LocalDateTime lastPasswordResetDate = LocalDateTime.now();
        user.setLastPasswordResetDate(lastPasswordResetDate);
        user.setLastLoginDate(lastLoginDate);
        list.add(user);
        stringRedisTemplate.opsForValue().set("Avengers", gson.toJson(list), Duration.ofSeconds(30));
        
        List<User> persistUserList = gson.fromJson(stringRedisTemplate.opsForValue().get("Avengers"), new TypeToken<List<User>>(){}.getType());
        Assert.assertTrue(persistUserList.size() == 1);
        Assert.assertEquals("IronMan",persistUserList.get(0).getName());
    }

    @Test
    public void testObjectTemplatete() {
    	User user = new User();
    	user.setName("BatManO");
    	user.setEnabled(true);
    	LocalDateTime lastLoginDate = LocalDateTime.now();
    	user.setLastLoginDate(lastLoginDate);
    	user.setUpdateTime(new Date());    	
    	redisTemplate.opsForValue().set("BatManO", user,Duration.ofSeconds(30));        
       
        User persistUser = (User)redisTemplate.opsForValue().get("BatManO");
        Assert.assertEquals(user.getName(),persistUser.getName());
        Assert.assertTrue(persistUser.getEnabled());
        Assert.assertEquals(user.getLastLoginDate().getSecond(),persistUser.getLastLoginDate().getSecond());
        
        
        List<User> list = new ArrayList<>();
        user = new User();
        user.setName("IronManO");
        LocalDateTime lastPasswordResetDate = LocalDateTime.now();
        user.setLastPasswordResetDate(lastPasswordResetDate);
        user.setLastLoginDate(lastLoginDate);
        list.add(user);
        redisTemplate.opsForValue().set("AvengersO", list,Duration.ofSeconds(30));
        
        @SuppressWarnings("unchecked")
		List<User> persistUserList = (List<User>)(redisTemplate.opsForValue().get("AvengersO"));
        Assert.assertTrue(persistUserList.size() == 1);
        Assert.assertEquals("IronManO",persistUserList.get(0).getName());  
    } 
}

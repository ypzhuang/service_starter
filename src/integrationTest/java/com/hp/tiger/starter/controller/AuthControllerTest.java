/**
 * created Dec 27, 2018 by ypzhuang
 * 
 * 测试 AuthController
 */

package com.hp.tiger.starter.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.hp.tiger.starter.security.JwtAuthenticationRequest;
import com.hp.tiger.starter.security.service.JwtAuthenticationResponse;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class AuthControllerTest {	
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	
	Gson gson = new Gson();
	private String token;
	
	@Autowired
	private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

	@Before
	public void setup() throws Exception{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				.addFilter(springSecurityFilterChain).build();
		
		String ret = this.mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(new JwtAuthenticationRequest("admin","12345678"))))
                .andReturn().getResponse().getContentAsString();
    	JwtAuthenticationResponse response = gson.fromJson(ret, JwtAuthenticationResponse.class);
    	this.token = response.getToken();
	}	


    @Test
    public void testLogin_shouldReturnToken() throws Exception {
    	this.mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(new JwtAuthenticationRequest("admin","12345678"))))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")))
                .andDo(print());
    }
    
    @Test
    public void testGetUserWithToken() throws Exception {    	
    	this.mockMvc.perform(get("/api/auth/user").header("Authorization", token)).andExpect(status().isOk());    	
    }
}

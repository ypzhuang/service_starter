/**
 * created Feb 2, 2019 by ypzhuang
 * 
 * TODO 功能描述
 */

package com.bdease.spm.controller.app;


import java.time.Duration;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.util.Asserts;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.LiteDevice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bdease.spm.entity.MiniProgramUser;
import com.bdease.spm.entity.User;
import com.bdease.spm.ex.ApplicationException;
import com.bdease.spm.ex.ServerException;
import com.bdease.spm.security.JwtTokenUtil;
import com.bdease.spm.security.service.JwtAuthenticationResponse;
import com.bdease.spm.service.IMiniProgramUserService;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.vo.EncryptedMiniUserVO;
import com.bdease.spm.wechat.WXBizDataCrypt;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/app/auth")
public class MiniAuthController extends MiniBaseController {
	@Value("${app.mini.loginURL}")
	private String loginURLFormat;
	
	@Value("${app.mini.appId}")
	private String appId;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IMiniProgramUserService miniProgramUserService;
	
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    @Value("${jwt.expiration}")
    private Long expiration;
	
	@GetMapping
	@ApiOperation(value = "小程序登录")
	public JwtAuthenticationResponse login(@ApiParam(value = "jsCode",required = true) @RequestParam(required = true) String code) {
		String loginURL = String.format(loginURLFormat, code);		
		ResponseEntity<String> response = restTemplate.getForEntity(loginURL, String.class);
		log.debug("{}",response);
		if (response.getStatusCode().equals(HttpStatus.OK)) {
			String body = response.getBody();
			JSONObject jsonObject = new JSONObject(body);
			try {
				String sessionKey =  jsonObject.getString("session_key");
				String openId =  jsonObject.getString("openid");
				log.debug("openId:{},sessionKey:{}", openId, sessionKey);
				MiniProgramUser miniProgramUser = this.miniProgramUserService.getMiniProgramUserByOpenId(openId);
				if (miniProgramUser == null)  {
					miniProgramUser = new MiniProgramUser();
					miniProgramUser.setOpenId(openId);
					miniProgramUserService.saveOrUpateMiniProgramUser(miniProgramUser);					
				} 
				
				// String token = (String)redisTemplate.opsForValue().get(openId);
				//if(token == null) {
					// Generate token					
					User user = userService.getUserByOpenId(openId);
					UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
					String token = jwtTokenUtil.generateToken(userDetails, LiteDevice.NORMAL_INSTANCE);
				//} 
				
				// save openid,session-key to redis			
				redisTemplate.opsForValue().set(token, sessionKey, Duration.ofSeconds(expiration));
				// redisTemplate.opsForValue().set(openId, token, Duration.ofSeconds(expiration));
			   
				return new JwtAuthenticationResponse(token);
			} catch(JSONException e) {
				Integer errcode =  jsonObject.getInt("errcode");
				String errmsg = jsonObject.getString("errmsg");
				switch(errcode) {
					case -1:
						throw new ApplicationException("微信系统繁忙，请稍候再试");
					case 40163: throw new ServerException("code 已使用");
					case 40029: throw new ServerException("code 无效");
					case 45011: throw new ApplicationException("过于频繁请求");
					default:
						throw new ApplicationException("微信服务器未知错误:" + errmsg);

				}
			}			
		}
		throw new ServerException("请求微信jscode2session接口未知错误");
	}
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	@PostMapping
	@ApiOperation(value = "新增或者更新小程序用户信息")
	@PreAuthorize("hasAnyRole('ROLE_GUEST')")
	public MiniProgramUser createOrUpdateMiniUser(@Valid @RequestBody(required = true) EncryptedMiniUserVO encryptedMiniUserVO) {
		String authToken = request.getHeader(this.tokenHeader);
		String sessionKey = encryptedMiniUserVO.getSessionKey();
		
		if(authToken != null) {
			String persistSessionKey = (String)redisTemplate.opsForValue().get(authToken);
			if (persistSessionKey != null) sessionKey = persistSessionKey;
		}
		
		WXBizDataCrypt core = new WXBizDataCrypt(this.appId);
		log.debug("encryptedData:{}",encryptedMiniUserVO.getEncryptedData());
		log.debug("sessionKey:{}",sessionKey);
		log.debug("iv:{}",encryptedMiniUserVO.getIv());
		String decryptedData = core.decrypt(encryptedMiniUserVO.getEncryptedData(), sessionKey,
				encryptedMiniUserVO.getIv());
		log.debug("decryptedData:{}",decryptedData);
		Asserts.check(!StringUtils.isEmpty(decryptedData), "非法的数据请求");
		MiniProgramUser user = new Gson().fromJson(decryptedData, MiniProgramUser.class);
		return miniProgramUserService.saveOrUpateMiniProgramUser(user);
	}	
}

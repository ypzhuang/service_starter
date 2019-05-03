package com.bdease.spm.controller.app.employee;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.security.JwtAuthenticationRequest;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.security.service.JwtAuthenticationResponse;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/app/emp/v1")
@Api(tags={"MiniEmp"})
public class MiniAuthController extends MiniBaseController {
	
	@Value("${app.defaultPassword}")
	private String defaultPasswd;
	
	@Autowired
	private IShopService shopService;
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/auth")
    @ApiOperation(value = "店员/店长登陆")
	@ApiResponses(value = {			 
			@ApiResponse(code = 307, message = "跳转到修改密码功能"),
			@ApiResponse(code = 308, message = "跳转到选择当前店铺功能")
	})
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {  
		 ResponseEntity<JwtAuthenticationResponse> responseEntity = super.authController.createAuthenticationToken(authenticationRequest, device);
		 JwtAuthenticationResponse response = responseEntity.getBody();
		 if(authenticationRequest.getPassword().equals(defaultPasswd)) {
			 return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).body(response); //307
		 }
		 List<Long> shopIds = this.shopService.getOwnShopIds(JwtUser.currentUserId());
		 if(shopIds.size() > 1) {
			 if(userService.getActiveShopOfCurrentUser() == null) {
				 return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).body(response); //308
			 }			
		 } else if(shopIds.size() == 1) { //只有一个店铺，自动设置当前店铺
			 userService.setActiveShopOfCurrentUser(shopIds.get(0));
		 }		 
		 return responseEntity;		 
	}
	
	
	
}

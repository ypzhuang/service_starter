package com.bdease.spm.controller.app.employee;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.entity.User;
import com.bdease.spm.security.JwtAuthenticationRequest;
import com.bdease.spm.security.JwtTokenUtil;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.security.service.JwtAuthenticationResponse;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.vo.ChangePasswdVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/app/emp/v1/auth")
@Api(tags={"MiniEmp"})
public class MiniAuthController extends MiniBaseController {
	
	@Value("${app.defaultPassword}")
	private String defaultPasswd;
	
	@Autowired
	private IShopService shopService;
	
	@Autowired
	private IUserService userService;	
	 
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
	
	@PostMapping
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
	
	@PutMapping
	@ApiOperation(value = "修改密码")
	@PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
	void changePassword(@Valid @RequestBody ChangePasswdVO changePasswordVO) {
		Asserts.check(!changePasswordVO.getNewPassword().equals(changePasswordVO.getOldPassword()),"新旧密码不能相同");
	    Long currentUserId = JwtUser.currentUserId();
	    User user = userService.getUser(currentUserId);	    
	    Asserts.check(new BCryptPasswordEncoder().matches(changePasswordVO.getOldPassword(), user.getPassword()),"旧密码错误。");	   
	    userService.updatePassword(user.getId(),changePasswordVO.getNewPassword());  
	}
	
	@RequestMapping(value = "user", method = RequestMethod.GET)
	@ApiOperation(value = "我是谁")
	@PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
	public JwtUser getAuthenticatedUser(HttpServletRequest request) {
		String token = request.getHeader(tokenHeader);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
		return user;
	}	
}

package com.bdease.spm.controller.app.employee;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.entity.Shop;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/app/emp/v1/shops")
@Api(tags={"MiniEmp"})
// @PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
public class MiniShopController extends MiniBaseController {
	
	@Value("${app.defaultPassword}")
	private String defaultPasswd;
	
	@Autowired
	private IShopService shopService;
	
	@Autowired
	private IUserService userService;
	
		
	@PutMapping("/{shopId}")
    @ApiOperation(value = "设置当前店铺")
	public Shop setActiveShopOfCurrentUser(@PathVariable Long shopId) {
		return userService.setActiveShopOfCurrentUser(shopId);
	}
	
	@GetMapping
    @ApiOperation(value = "我的店铺")
	public List<Shop> getShopsOfCurrentUser() {
		List<Long> shopIds = shopService.getOwnShopIds(JwtUser.currentUserId());
		return shopIds.stream().map(shopId -> shopService.getShop(shopId)).collect(Collectors.toList());
	}
	
	@GetMapping("/current")
    @ApiOperation(value = "我的当前店铺")
	public Shop getCurrentShopOfCurrentUser() {
		return userService.getActiveShopOfCurrentUser();		
	}
}

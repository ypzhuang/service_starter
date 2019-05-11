package com.bdease.spm.controller.app.employee;


import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.entity.Shop;

import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IOrderService;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.vo.PerformanceVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/app/emp/v1/performances")
@Api(tags={"MiniEmp"})
// @PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
public class MiniPerformanceController extends MiniBaseController{
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IShopService shopService;
	
	@GetMapping
	@ApiOperation(value = "查询绩效")
	public PerformanceVO searchPerformances() {
		Long userId = JwtUser.currentUserId();		
		Shop currentShop = userService.getActiveShopOfCurrentUser();	
		Shop shop = shopService.findShop(currentShop.getId());
		List<Long> userIds = new ArrayList<>();
		userIds.add(userId);		
		if(userId.equals(shop.getShopAdmin())) {				
			userIds.addAll(shop.getShopUsers());
		}		
		return orderService.getPerformance(userIds);		
	}	
}

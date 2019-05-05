package com.bdease.spm.controller.app.employee;


import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.entity.MiniProgramUser;
import com.bdease.spm.entity.Shop;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/app/emp/v1/guests")
@Api(tags={"MiniEmp"})
public class MiniGuestController extends MiniBaseController{
	@Autowired
	private IUserService userServie;
	
	@GetMapping
    @ApiOperation(value = "分页查询客户")
	@PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
    public IPage<MiniProgramUser> getBrokersByPage(
            @ApiParam(value = "客户姓名、手机号或微信昵称",required = false) @RequestParam(required = false) String user,          
            @ApiParam(value = "N月未拍照", required = false) @RequestParam(required = false) Integer monthsOfNoPictures,
            @ApiParam(value = "N月没下单", required = false) @RequestParam(required = false) Integer monthsOfNoOrders,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
		 Shop shop = userServie.getActiveShopOfCurrentUser();
		 Asserts.check(shop != null, "当前用户:%d无当前店铺信息.",JwtUser.currentUserId());
		 Long shopId = shop.getId();
		return super.guestController.getGuestByPage(user, shopId, monthsOfNoPictures, monthsOfNoOrders, current, size);       
    }
}

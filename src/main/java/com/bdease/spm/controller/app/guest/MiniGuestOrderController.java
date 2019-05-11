/**
 * created Feb 2, 2019 by ypzhuang
 * 
 *  功能描述
 */

package com.bdease.spm.controller.app.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.entity.GuestOrder;

import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IOrderItemService;
import com.bdease.spm.service.IOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/app/guest/v1/orders")
@Api(tags={"MiniGuest"})
@PreAuthorize("hasAnyRole('ROLE_GUEST')")
public class MiniGuestOrderController extends MiniBaseController {
	@Autowired
	private IOrderItemService orderItemService;
	
	@Autowired
	private IOrderService orderService;
	
	@GetMapping
    @ApiOperation(value = "分页查询订单")
    public IPage<GuestOrder> getPhotosByPage(           
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {		
		IPage<GuestOrder> ordersPage =  this.orderService.getOrdersByPage(JwtUser.currentUserId(), current, size);
		ordersPage.getRecords().forEach(order -> {
			order.setItems(this.orderItemService.getOderItemsByOrderId(order.getId()));
		});
		return ordersPage;
    }	
}

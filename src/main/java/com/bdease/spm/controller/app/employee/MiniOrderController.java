package com.bdease.spm.controller.app.employee;


import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.entity.GuestOrder;
import com.bdease.spm.vo.OrderVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/app/emp/v1/orders")
@Api(tags={"MiniEmp"})
public class MiniOrderController extends MiniBaseController{
	
	@GetMapping
    @ApiOperation(value = "分页查询订单")
    public IPage<GuestOrder> getPhotosByPage(
            @ApiParam(value = "客户ID",required = true) @RequestParam(required = true) Long miniProgramUserId,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
		IPage<GuestOrder> ordersPage =  super.orderController.getOrdersByPage(miniProgramUserId, current, size);
		ordersPage.getRecords().forEach(order -> {
			order.setItems(super.orderController.getOderItemsByOrderId(order.getId()));
		});
		return ordersPage;
    }
	
	@PostMapping
	@ApiOperation(value = "创建订单")
	public void createOrder(@Valid @RequestBody OrderVO orderVO) {
		super.orderController.createOrder(orderVO);
	}
}

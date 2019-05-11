package com.bdease.spm.controller.app.employee;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.entity.GuestOrder;
import com.bdease.spm.entity.Shop;
import com.bdease.spm.entity.User;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IOrderItemService;
import com.bdease.spm.service.IOrderService;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.vo.OrderVO;
import com.bdease.spm.vo.SaleRecordVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/app/emp/v1/orders")
@Api(tags={"MiniEmp"})
@PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
public class MiniOrderController extends MiniBaseController{
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IShopService shopService;
	
	@Autowired
	private IOrderItemService orderItemService;
	
	@GetMapping
    @ApiOperation(value = "分页查询订单")
    public IPage<GuestOrder> getPhotosByPage(
            @ApiParam(value = "客户ID",required = true) @RequestParam(required = true) Long miniProgramUserId,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
		IPage<GuestOrder> ordersPage =  this.orderService.getOrdersByPage(miniProgramUserId, current, size);
		ordersPage.getRecords().forEach(order -> {
			order.setItems(this.orderItemService.getOderItemsByOrderId(order.getId()));
		});
		return ordersPage;
    }
		
	@PostMapping
	@ApiOperation(value = "创建订单")
	public void createOrder(@Valid @RequestBody OrderVO orderVO) {
		this.orderService.createOrder(orderVO);
	}	
	
	@GetMapping("/3days")
    @ApiOperation(value = "分页查询3日销售记录 ")
	public IPage<SaleRecordVO>  getSaleRecordsByPage(
			@ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size) {
		 
		  Long currentUserId = JwtUser.currentUserId();
		  Shop currentShop = this.userService.getActiveShopOfCurrentUser();
		  Long shopId = null;
		  Long soldBy = null;
		  User shopAdmin = this.shopService.getShopAdmin(currentShop.getId());
		  log.debug("Current userId: {}, shopId:{}, shopAdminId:{}", currentUserId, currentShop.getId(), shopAdmin.getId());
		  if(currentUserId.equals(shopAdmin.getId())) { //店长
			  shopId = currentShop.getId();
		  } else {
			  soldBy = currentUserId;
		  }		   
		  return this.orderService.pageSaleRecord(shopId, soldBy,current,size);		
	}
}

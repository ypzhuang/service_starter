package com.bdease.spm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.GuestOrder;
import com.bdease.spm.entity.OrderItem;
import com.bdease.spm.service.IOrderItemService;
import com.bdease.spm.service.IOrderService;
import com.bdease.spm.vo.OrderVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@RestController
@RequestMapping("/api/v1/orders")
@Api(tags = { "Order" })
public class OrderController extends BaseController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderItemService orderItemService;

    @GetMapping
    @ApiOperation(value = "分页查询订单")
    public IPage<GuestOrder> getOrdersByPage(
            @ApiParam(value = "客户ID",required = true) @RequestParam(required = true) Long miniProgramUserId,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
        Page<GuestOrder> page = new Page<>(current,size);

        return this.orderService.page(page,new LambdaQueryWrapperAdapter<GuestOrder>()
                .eq(GuestOrder::getMiniUserId,miniProgramUserId)
        );
    }

    @GetMapping("/{id}/items")
    @ApiOperation(value = "查询订单项目明细")
    public List<OrderItem> getOderItemsByOrderId(@PathVariable Long id
    ) {
        return this.orderItemService.list(new LambdaQueryWrapperAdapter<OrderItem>().eq(OrderItem::getOrderId,id));
    }
    
    public void createOrder(OrderVO orderVO) {
    	 this.orderService.createOrder(orderVO);
    }
}

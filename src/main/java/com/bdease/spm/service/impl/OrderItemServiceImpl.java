package com.bdease.spm.service.impl;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.OrderItem;
import com.bdease.spm.mapper.OrderItemMapper;
import com.bdease.spm.service.IOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单项目 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {
	@Override
	public List<OrderItem> getOderItemsByOrderId(Long id) {
	    return list(new LambdaQueryWrapperAdapter<OrderItem>().eq(OrderItem::getOrderId,id));
	}
}

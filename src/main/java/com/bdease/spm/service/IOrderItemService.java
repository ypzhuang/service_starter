package com.bdease.spm.service;

import com.bdease.spm.entity.OrderItem;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单项目 服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
public interface IOrderItemService extends IService<OrderItem> {	
	List<OrderItem> getOderItemsByOrderId(Long id);
}

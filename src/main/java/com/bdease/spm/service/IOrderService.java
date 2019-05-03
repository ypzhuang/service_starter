package com.bdease.spm.service;

import com.bdease.spm.entity.GuestOrder;
import com.bdease.spm.vo.OrderVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
public interface IOrderService extends IService<GuestOrder> {	
	void createOrder(OrderVO orderVO);
	
	List<Map<String, Object>> countGroupByGuestId();
	
	LocalDate getLatestOrderDate(Long guestId);
}

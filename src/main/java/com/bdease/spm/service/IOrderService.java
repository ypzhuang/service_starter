package com.bdease.spm.service;

import com.bdease.spm.entity.GuestOrder;
import com.bdease.spm.vo.OrderVO;
import com.bdease.spm.vo.PerformanceVO;
import com.bdease.spm.vo.SaleRecordVO;
import com.bdease.spm.vo.DailySaleVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
	
	List<DailySaleVO> getDailySales(Long userId);
	
	PerformanceVO getPerformance(List<Long> userIds);
	
	IPage<SaleRecordVO> pageSaleRecord(Page<SaleRecordVO> page,Long shopId, Long soldBy);
}

package com.bdease.spm.mapper;

import com.bdease.spm.entity.GuestOrder;
import com.bdease.spm.vo.DailySaleVO;
import com.bdease.spm.vo.SaleRecordVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
public interface OrderMapper extends BaseMapper<GuestOrder> {
	
	@Select("SELECT count(*) count,mini_user_id FROM guest_order where del_flag = '0' group by mini_user_id")
	List<Map<String, Object>> countGroupByGuestId();
	
	@Select("SELECT max(order_date) as latest_order_date from guest_order where del_flag = '0' and mini_user_id = #{guestId}")
	LocalDate getLatestOrderDate(Long guestId);
	
	@Select("select sum(pay_amount) as amount,order_date from guest_order \n" + 
			"where 1 = 1\n" + 
			"and sold_by = #{userId} " + 
			"and order_date > last_day(date_sub(now(),interval 1 month))\n" + 			
			"group by sold_by, order_date\n" + 
			"order by order_date")
	List<DailySaleVO> getDailySales(Long userId);
	
	IPage<SaleRecordVO> pageSaleRecord(Page<SaleRecordVO> page,
            @Param("shopId") Long shopId,
            @Param("soldBy") Long soldBy);
	
}

package com.bdease.spm.mapper;

import com.bdease.spm.entity.GuestOrder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


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
	
}

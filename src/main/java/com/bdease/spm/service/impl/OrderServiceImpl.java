package com.bdease.spm.service.impl;

import com.bdease.spm.entity.GuestOrder;
import com.bdease.spm.mapper.OrderMapper;
import com.bdease.spm.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, GuestOrder> implements IOrderService {

}

package com.bdease.spm.service.impl;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.Goods;
import com.bdease.spm.entity.GuestOrder;
import com.bdease.spm.entity.MiniProgramUser;
import com.bdease.spm.entity.OrderItem;
import com.bdease.spm.entity.Shop;
import com.bdease.spm.entity.User;
import com.bdease.spm.entity.enums.GuestStatus;
import com.bdease.spm.entity.enums.OrderStatus;
import com.bdease.spm.mapper.OrderMapper;
import com.bdease.spm.service.IGoodsService;
import com.bdease.spm.service.IMiniProgramUserService;
import com.bdease.spm.service.IOrderItemService;
import com.bdease.spm.service.IOrderService;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.vo.DailySaleVO;
import com.bdease.spm.vo.OrderVO;
import com.bdease.spm.vo.PerformanceVO;
import com.bdease.spm.vo.SaleRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	protected  final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMiniProgramUserService guestService;
	
	@Autowired
	private IShopService shopService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IGoodsService goodsService;
	
	@Autowired
	private IOrderItemService orderItemService;
	
	@Transactional
	@Override
	public void createOrder(OrderVO orderVO) {
		Long shopId = orderVO.getShopId();
		log.debug("Check shopId:{}",shopId);
		Shop shop = checkShopId(shopId);
		Long guestId = orderVO.getMiniUserId();
		log.debug("Check guestId:{},shopId:{}",guestId, shopId);
		checkGuestId(guestId,shopId);		
		log.debug("Check saleId:{},shopId:{}",guestId, shopId);
		User sale = checkSaleId(orderVO.getSoldBy(),shopId);
		
		GuestOrder order = new GuestOrder();
		BeanUtils.copyProperties(orderVO, order);
		order.setOrderDate(LocalDate.now());
		Integer goodsQuantity = orderVO.getOrderItems().stream().mapToInt(item -> item.getQuantity()).sum();
		order.setGoodsQuantity(goodsQuantity);
		order.setTotalAmount(order.getDiscountAmount().add(order.getPayAmount()));
		order.setStatus(OrderStatus.PAYED);
		order.setShopName(shop.getName());
		order.setSoldByName(sale.getName());		
		this.save(order);
		
		Long orderId = order.getId();
		
		List<OrderItem> items = orderVO.getOrderItems().stream().map(itemVO -> {
			OrderItem item = new OrderItem();
			Long goodsId = itemVO.getGoodsId();
			item.setOrderId(orderId);
			item.setPrice(itemVO.getPrice());
			item.setQuantity(itemVO.getQuantity());
			item.setAmount(item.getPrice().multiply(new BigDecimal(item.getQuantity())));			
			Goods goods = goodsService.getGoods(goodsId);			
			item.setIdentifier(goods.getIdentifier());
			item.setImgUrl(goods.getImgUrl());
			item.setName(goods.getName());
			item.setSpec(goods.getSpec());			
			return item;
		}).collect(Collectors.toList());
		
		orderItemService.saveBatch(items);		
	}

	private Shop checkShopId(Long shopId) {
		return shopService.getOpeningShop(shopId);		
	}
	
	private MiniProgramUser checkGuestId(Long guestId, Long shopId) {
		MiniProgramUser user =guestService.getOne(new LambdaQueryWrapperAdapter<MiniProgramUser>()
				.eq(MiniProgramUser::getId, guestId)
				.eq(MiniProgramUser::getShopId, shopId)
				.eq(MiniProgramUser::getStatus, GuestStatus.NORMAL));
		Asserts.check(user != null, "不存在或异常的客户ID:%d,店铺ID:%d",guestId,shopId);
		return user;
	}
	
	private User checkSaleId(Long saleId, Long shopId) {
		Shop shop = shopService.getOpeningShop(shopId);
		Asserts.check(shop.getShopUsers().contains(saleId) || saleId.equals(shop.getShopAdmin()), "非法的销售员ID%s",saleId);		
		return userService.getById(saleId);
	}

	@Override
	public List<Map<String, Object>> countGroupByGuestId() {
		return this.baseMapper.countGroupByGuestId();	
	}

	@Override
	public LocalDate getLatestOrderDate(Long guestId) {
		return this.baseMapper.getLatestOrderDate(guestId);		
	}

	@Override
	public List<DailySaleVO> getDailySales(Long userId) {
		return this.baseMapper.getDailySales(userId);
	}
	
	public PerformanceVO getPerformance(List<Long> userIds) {
		PerformanceVO performance = new PerformanceVO();
		for (Long userId : userIds) {
			User user = userService.getUser(userId);
			performance.addDailySale(userId, user.getName(), getDailySales(userId));
		}
		return performance;
	}

	@Override
	public IPage<SaleRecordVO> pageSaleRecord(Long shopId, Long soldBy,Integer current, Integer size) {
		Page<SaleRecordVO> page = new Page<>(current,size);
		return this.baseMapper.pageSaleRecord(page, shopId, soldBy);		
	}
	
	@Override
	public IPage<GuestOrder> getOrdersByPage(Long miniProgramUserId, Integer current, Integer size) {
		Page<GuestOrder> page = new Page<>(current,size);	
	    return this.page(page,new LambdaQueryWrapperAdapter<GuestOrder>()
	            .eq(GuestOrder::getMiniUserId,miniProgramUserId)
	    );
	}
}

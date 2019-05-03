package com.bdease.spm.service;

import com.bdease.spm.entity.Shop;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bdease.spm.entity.User;
import com.bdease.spm.vo.ShopVO;

/**
 * <p>
 * 店铺信息 服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-14
 */
public interface IShopService extends IService<Shop> {
	List<Shop> getShopsByUserId(Long userId);

	void deleteShop(Long id);

	Shop getShop(Long id);
	
	Shop findShop(Long id);
	
	Shop getOpeningShop(Long id);

	List<Long> getOwnShopIds(Long userId);

    Shop saveOrUpdateShop(Long id, ShopVO shopVO);

	void addUserInformations(Shop shop);

	User getShopAdmin(Long shopId);

	User getShopManager(Long shopId);

	List<User> getShopUsers(Long shopId);
}

package com.bdease.spm.service.impl;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.*;
import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.entity.enums.ShopStatus;
import com.bdease.spm.mapper.ShopMapper;
import com.bdease.spm.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bdease.spm.utils.SetOperation;
import com.bdease.spm.vo.ShopVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 店铺信息 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-14
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

	protected  final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserShopService userShopService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private IAuthorityService authorityService;

	@Autowired
	private IUserAuthorityService userAuthorityService;

	@Autowired
	private IUserService userService;
	
	@Override
	public List<Shop> getShopsByUserId(Long userId) {
		List<UserShop> userShops = userShopService.list(new LambdaQueryWrapperAdapter<UserShop>()
				.eq(UserShop::getUserId, userId));
		
		List<Shop> shops = new ArrayList<Shop>();
		userShops.forEach(us -> {
			shops.addAll(this.baseMapper.selectList(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getId, us.getShopId())));
		});		
		return shops;
	}

	@Transactional
	@Override
	public void deleteShop(Long id) {
		Shop shop = getShop(id);
		this.removeById(shop.getId());
	}

	@Override
	public Shop getShop(Long id) {
		Shop shop = this.getById(id);
		Asserts.check(shop != null, "不存在的店铺id:%d",id);
		return shop;
	}

	@Override
	public Shop findShop(Long id) {
		Shop shop =  getShop(id);
		addUserInformations(shop);
		return shop;
	}
	
	@Override
	public Shop getOpeningShop(Long id) {
		Shop shop = this.getById(id);
		Asserts.check(shop != null && ShopStatus.OPEN.equals(shop.getStatus()), "不存在或者暂停协议的店铺id:%d",id);
		addUserInformations(shop);
		return shop;
	}

	@Override
	public void addUserInformations(Shop shop) {
		User shopAdmin = getShopAdmin(shop.getId());
		if(shopAdmin != null) shop.setShopAdmin(shopAdmin.getId());

		User shopManager = getShopManager(shop.getId());
		if(shopManager != null)shop.setShopManager(shopManager.getId());

		List<User>  shopUsers = getShopUsers(shop.getId());
		shop.setShopUsers(shopUsers.stream().map(u -> u.getId()).collect(Collectors.toList()));
	}

	public User getShopAdmin(Long shopId) {
		List<User> shopAdmins = userService.findUsers(shopId, AuthorityName.ROLE_SHOP_ADMIN);
		Asserts.check(shopAdmins.size() <= 1,"异常：不支持一店多店长");
		if(shopAdmins.size() == 1) {
			return shopAdmins.get(0);
		}
		return null;
	}

	public User getShopManager(Long shopId) {
		List<User> shopMangers = userService.findUsers(shopId,AuthorityName.ROLE_MANAGER);
		log.debug("shopMangers:{}",shopMangers);
		Asserts.check(shopMangers.size() <= 1,"异常：不支持一店多管理员");
		if(shopMangers.size() == 1) {
			return shopMangers.get(0);
		}
		return null;
	}

	public List<User> getShopUsers(Long shopId) {
		List<User>  shopUsers = userService.findUsers(shopId,AuthorityName.ROLE_SHOP_USER);
		return shopUsers;
	}


	@Override
	public List<Long> getOwnShopIds(Long userId) {
		String key = String.format("shops_id:%d",userId);
		Gson gson = new Gson();
		List<Long> shopIds = gson.fromJson(stringRedisTemplate.opsForValue().get(key), new TypeToken<List<Long>>(){}.getType());

		if(shopIds == null) {
			List<Shop> shops = getShopsByUserId(userId);
			shopIds = shops.stream().map(s -> s.getId()).collect(Collectors.toList());
			stringRedisTemplate.opsForValue().set(key, gson.toJson(shopIds), Duration.ofSeconds(86400));
		}
		log.debug("UserId:{} in ShopId:{}",userId, shopIds);
		return shopIds;
	}

	@Transactional
	@Override
	public Shop saveOrUpdateShop(Long id, ShopVO shopVO) {
		checkShopRepetition(shopVO.getName(),id);

		Shop shop = new Shop();
		BeanUtils.copyProperties(shopVO,shop);
		shop.setId(id);
		this.saveOrUpdate(shop);

		//user ,authority
		Long shopAdminUserId = shopVO.getShopAdmin();
		log.debug("Process shopAdmin:{}",shopAdminUserId);
		if(shopAdminUserId != null) {
			modifyUserRoleRelation(shopAdminUserId,AuthorityName.ROLE_SHOP_ADMIN);
		} else {
			User shopAdmin = getShopAdmin(shop.getId());
			if(shopAdmin != null) {
				userShopService.remove(new LambdaQueryWrapperAdapter<UserShop>()
						.eq(UserShop::getUserId,shopAdmin.getId())
						.eq(UserShop::getShopId,shop.getId())
				);
			}
		}


		Long shopManagerId = shopVO.getShopManager();
		log.debug("Process shopManager:{}",shopManagerId);
		if(shopManagerId != null) {
			modifyUserRoleRelation(shopManagerId, AuthorityName.ROLE_MANAGER);
		} else {
			User shopManager = getShopManager(shop.getId());
			if(shopManager != null) {
				userShopService.remove(new LambdaQueryWrapperAdapter<UserShop>()
						.eq(UserShop::getUserId,shopManager.getId())
						.eq(UserShop::getShopId,shop.getId())
				);
			}

		}

		List<Long> shopUserIds = shopVO.getShopUsers();
		log.debug("Process shopUsers:{}",shopUserIds);
		shopUserIds.forEach(userId -> modifyUserRoleRelation(userId,AuthorityName.ROLE_SHOP_USER));

		if(shopUserIds.isEmpty()) {
			List<User> shopUsers = getShopUsers(shop.getId());
			shopUsers.forEach(user -> {
				userShopService.remove(new LambdaQueryWrapperAdapter<UserShop>()
						.eq(UserShop::getUserId,user.getId())
						.eq(UserShop::getShopId,shop.getId())
				);
			});
		}

		//user,shop
		userShopService.createUserShop(shop.getId(),shopAdminUserId);
		userShopService.createUserShop(shop.getId(),shopManagerId);
		userShopService.createOrUpdateUserShop(shop.getId(),shopUserIds);

		return this.findShop(shop.getId());
	}

	private void modifyUserRoleRelation(Long userId,AuthorityName toBeRole) {
		List<Authority> pAuthorities = authorityService.selectByUserId(userId);
		List<AuthorityName> pAuthorityNames = pAuthorities.stream().map(authority ->
			authority.getName()
		).collect(Collectors.toList());
		List<AuthorityName> toAuthorityNames = Arrays.asList(toBeRole);

		List<AuthorityName> toRemove = SetOperation.minus(pAuthorityNames,toAuthorityNames);
		List<AuthorityName> toCreate = SetOperation.minus(toAuthorityNames,pAuthorityNames);

		toRemove.forEach(a -> userAuthorityService.removeUserAuthorityRelation(userId,a));

		List<Authority> toCreateAuthorities= toCreate.stream().map(authorityName -> authorityService.getOrCreateAuthorityByName(authorityName))
		.collect(Collectors.toList());
		userAuthorityService.addUserAuthorityRelation(userId,toCreateAuthorities);
	}

	private void checkShopRepetition( String shopName, Long forUpdateId) {
		Shop shop = this.getOne(new LambdaQueryWrapperAdapter<Shop>()
				.eq(Shop::getName, shopName.trim()));
		Asserts.check(shop == null || forUpdateId !=null && shop.getId().equals(forUpdateId) ,"重复的店铺名称:%s", shopName);
	}
}

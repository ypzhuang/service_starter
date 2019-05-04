package com.bdease.spm.service.impl;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.Authority;
import com.bdease.spm.entity.Shop;
import com.bdease.spm.entity.User;
import com.bdease.spm.entity.UserAuthority;
import com.bdease.spm.entity.UserShop;
import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.mapper.UserMapper;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IAuthorityService;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserAuthorityService;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.service.IUserShopService;
import com.bdease.spm.utils.SetOperation;
import com.bdease.spm.vo.UserVO;
import com.google.gson.Gson;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.util.Asserts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
	protected  final Logger log = LoggerFactory.getLogger(getClass());

	@Value("${app.defaultPassword}")
	private String defaultPassword;
    
    @Autowired
    private IAuthorityService authorityService;
    
    @Autowired
    private IUserAuthorityService userAuthorityService;
    
    @Autowired
    private IShopService shopService;
    
    @Autowired
    private IUserShopService userShopService;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) {
    	Asserts.check(this.findUserByUsername(user.getUsername()) == null, "重复的用户名:%s",user.getUsername());
        this.save(user);
		userAuthorityService.addUserAuthorityRelation(user.getId(),user.getAuthorities());
	}



	public User findUserByUsername(String username) {
    	User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    	if (user == null) return null;
    	List<Authority> userAuthorities = authorityService.selectByUserId(user.getId());
    	user.setAuthorities(userAuthorities);  
    	return user;       
    }

    @Transactional
	@Override
	public void resetPassword(Long id) {
    	Asserts.check(this.getById(id) != null, "不存在的用户id：%d", id);
		User user = new User();
		user.setId(id);
		user.setPassword(new BCryptPasswordEncoder().encode(defaultPassword));
		// TODO send email
		user.setLastPasswordResetDate(LocalDateTime.now());
		user.setUpdatedBy(JwtUser.currentUserId());
		this.updateById(user);		
	}

    @Transactional
	@Override
	public void switchAccountStatus(Long id) {
		User pUser = this.getById(id);
		Asserts.check(pUser != null, "不存在的用户id：%d", id);
		User user = new User();
		user.setId(id);
		user.setEnabled(!pUser.getEnabled());
		user.setUpdatedBy(JwtUser.currentUserId());
		this.updateById(user);		
	}
    
    @Override
    public User getUserByOpenId(String openId) {
    	return this.baseMapper.getUserByOpenId(openId);
    }

	@Override
	public IPage<User> pageUsers(Page<User> page, String nameOrUserName, Long shopId, AuthorityName role, Boolean status) {
		return this.baseMapper.pageUsers(page, nameOrUserName, shopId, role, status);	
	}

	@Override
	public List<Shop> getCurrentUserShops() {
		Long id = JwtUser.currentUserId();
		return shopService.getShopsByUserId(id);
	}

	@Transactional
	@Override
	public void deleteUser(Long id) {
		User user = this.getById(id);
		Asserts.check(user != null, "不存在的用户id：%d", id);
		Asserts.check(!user.getEnabled(), "仅离职状态的员工才能删除", id);		
		this.baseMapper.deleteById(id);	
	}

	@Transactional
	@Override
	public User saveOrUpdateUser(Long forUpdateId, UserVO userVO) {
		checkUserRepetition(userVO.getUsername(),forUpdateId);
		//新增或者更新User
		if (forUpdateId == null) {
			User user = new User();
			user.setEnabled(userVO.getEnabled());
			user.setName(userVO.getName());
			user.setOnboardDate(userVO.getOnboardDate());
			user.setUsername(userVO.getUsername());
			user.setLastPasswordResetDate(LocalDateTime.now());
			user.setPassword(new BCryptPasswordEncoder().encode(defaultPassword));
			if(userVO.getRole() != null) {
				Authority authority = authorityService.getOrCreateAuthorityByName(userVO.getRole());		
				user.setAuthorities(Arrays.asList(authority));
			}
			this.saveUser(user);
			forUpdateId = user.getId();
			userVO.getShops().forEach(shopId -> {
				Asserts.check(shopService.getById(shopId) != null, "不存在的店铺id：%d", shopId);				
				UserShop userShop = new UserShop();
				userShop.setShopId(shopId);
				userShop.setUserId(user.getId());
				userShopService.save(userShop);
			});			
		} else { // Update
			User user = new User();
			user.setId(forUpdateId);
			user.setEnabled(userVO.getEnabled());
			user.setName(userVO.getName());
			user.setOnboardDate(userVO.getOnboardDate());
			user.setUsername(userVO.getUsername());
			this.updateById(user);
			
			List<Long> pAuthorityIds = authorityService.selectByUserId(user.getId()).stream().map(a -> a.getId()).collect(Collectors.toList());
			List<Long> uAuthorityIds = new ArrayList<>();
			if(userVO.getRole() != null) {
				Authority role = authorityService.getOne(new LambdaQueryWrapperAdapter<Authority>().eq(Authority::getName, userVO.getRole()));
				uAuthorityIds.add(role.getId());
			}
			
			List<Long> dAuthorityIds = SetOperation.minus(pAuthorityIds,uAuthorityIds);
			List<Long> aAuthorityIds = SetOperation.minus(uAuthorityIds,pAuthorityIds);
			
			for(Long d: dAuthorityIds) {
				userAuthorityService.remove(new LambdaQueryWrapperAdapter<UserAuthority>()
						.eq(UserAuthority::getUserId, user.getId())
						.eq(UserAuthority::getAuthorityId, d)
						);
			}			
			for(Long a: aAuthorityIds) {
				UserAuthority userAuthority = new UserAuthority();
	        	userAuthority.setAuthorityId(a);
	        	userAuthority.setUserId(user.getId());
	        	userAuthority.setCreatedBy(JwtUser.currentUserId());
	        	userAuthorityService.save(userAuthority);
			}	
			
			
			List<Long> pShopIds = shopService.getShopsByUserId(user.getId()).stream().map(s -> s.getId()).collect(Collectors.toList());
			List<Long> uShopIds = userVO.getShops();
			List<Long> dShopIds = SetOperation.minus(pShopIds,uShopIds);
			List<Long> aShopIds = SetOperation.minus(uShopIds,pShopIds);
			
			for(Long d: dShopIds) {
				Asserts.check(shopService.getById(d) != null, "不存在的店铺id：%d", d);	
				userShopService.remove(new LambdaQueryWrapperAdapter<UserShop>()
						.eq(UserShop::getUserId, user.getId())
						.eq(UserShop::getShopId, d));
			}
			for(Long a: aShopIds) {
				Asserts.check(shopService.getById(a) != null, "不存在的店铺id：%d", a);	
				UserShop userShop = new UserShop();
				userShop.setShopId(a);
				userShop.setUserId(user.getId());
				userShopService.save(userShop);	
			}			
		}
		
		User user = this.getById(forUpdateId);
		user.setShops(shopService.getShopsByUserId(user.getId()));
    	user.setAuthorityNames(authorityService.selectAuthorityNameByUserId(user.getId()));
		return user;
	}

	@Override
	public List<User> findUsers(Long shopId, AuthorityName role) {
		return this.baseMapper.findUsers(shopId,role);
	}

	private void checkUserRepetition(String userName, Long forUpdateId) {
		User user = this.getOne(new LambdaQueryWrapperAdapter<User>().eq(User::getUsername, userName));
		Asserts.check(user == null || forUpdateId != null && user.getId().equals(forUpdateId), "重复的手机号/用户名:%s",userName);
	} 

	@Override
	public Shop getActiveShopOfCurrentUser() {
		String key = String.format("ActiveShopId:%d", JwtUser.currentUserId());
		Gson gson = new Gson();
		return gson.fromJson(stringRedisTemplate.opsForValue().get(key), Shop.class);
	}

	@Override
	public Shop setActiveShopOfCurrentUser(Long shopId) {
		Gson gson = new Gson();
		String key = String.format("ActiveShopId:%d", JwtUser.currentUserId());
		List<Long> shopIds = this.shopService.getOwnShopIds(JwtUser.currentUserId());
		Asserts.check(shopIds.contains(shopId), "您不属于这家店？%d", shopId);
		Shop activeShop = this.shopService.getShop(shopId);
		stringRedisTemplate.opsForValue().set(key, gson.toJson(activeShop));
		return activeShop;
	}



	@Override
	public User getUser(Long id) {
		User user = this.getById(id);
		Asserts.check(user != null, "不存在的用户ID:%d",id);
		return user;
	}


    @Transactional
	@Override
	public void updatePassword(Long id, String newPassword) {
		User user = new User();
		user.setId(id);
		user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
		user.setLastPasswordResetDate(LocalDateTime.now());
		this.updateById(user);		
	}
}

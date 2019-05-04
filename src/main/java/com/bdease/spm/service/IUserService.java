package com.bdease.spm.service;

import com.bdease.spm.entity.Shop;
import com.bdease.spm.entity.User;
import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.vo.UserVO;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
public interface IUserService extends IService<User> {

    void saveUser(User user);  
	
	User findUserByUsername(String username);
	
	void resetPassword(Long id);
	
	void switchAccountStatus(Long id);
	
	User getUserByOpenId(String openId);
	
	User getUser(Long id);	
	
	IPage<User> pageUsers(Page<User> page, 
			String nameOrUserName, 
			Long shopId, 
			AuthorityName role,
			Boolean status);
	
	List<Shop> getCurrentUserShops();
	
	Shop getActiveShopOfCurrentUser();
	
	Shop setActiveShopOfCurrentUser(Long shopId);
	
	void deleteUser(Long id);

	User saveOrUpdateUser(Long id,UserVO userVO);

	List<User> findUsers(Long shopId,AuthorityName role);

	void updatePassword(Long id, String newPassword);
	
}

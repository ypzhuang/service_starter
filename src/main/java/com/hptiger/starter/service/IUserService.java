package com.hptiger.starter.service;

import com.hptiger.starter.entity.User;
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
	
	User getUser(Long id);	

	void deleteUser(Long id);

	void updatePassword(Long id, String newPassword);
	
}

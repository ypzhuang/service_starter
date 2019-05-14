package com.bdease.spm.service.impl;

import com.bdease.spm.entity.Authority;
import com.bdease.spm.entity.User;

import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.mapper.UserMapper;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IAuthorityService;
import com.bdease.spm.service.IUserAuthorityService;
import com.bdease.spm.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.http.util.Asserts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	public IPage<User> pageUsers(Page<User> page, String nameOrUserName, Long shopId, AuthorityName role, Boolean status, List<Long> shopIds) {
		return this.baseMapper.pageUsers(page, nameOrUserName, shopId, role, status, shopIds);	
	}


	@Transactional
	@Override
	public void deleteUser(Long id) {
		User user = this.getById(id);
		Asserts.check(user != null, "不存在的用户id：%d", id);
		Asserts.check(!user.getEnabled(), "仅离职状态的员工才能删除", id);		
		this.baseMapper.deleteById(id);	
	}

	

	@Override
	public List<User> findUsers(Long shopId, AuthorityName role) {
		return this.baseMapper.findUsers(shopId,role);
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

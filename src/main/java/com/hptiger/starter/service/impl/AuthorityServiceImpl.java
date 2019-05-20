package com.hptiger.starter.service.impl;

import com.hptiger.starter.adapter.LambdaQueryWrapperAdapter;
import com.hptiger.starter.entity.Authority;
import com.hptiger.starter.entity.User;
import com.hptiger.starter.entity.UserAuthority;
import com.hptiger.starter.entity.enums.AuthorityName;
import com.hptiger.starter.mapper.AuthorityMapper;
import com.hptiger.starter.security.JwtUser;
import com.hptiger.starter.service.IAuthorityService;
import com.hptiger.starter.service.IUserAuthorityService;
import com.hptiger.starter.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements IAuthorityService {
	@Autowired
	private IAuthorityService authorityService;

	@Autowired
	private AuthorityMapper authorityMapper;
	
	@Autowired
	private IUserAuthorityService userAuthorityService;
	
	@Autowired
	private IUserService userService;
	
	public Authority findAuthorityByName(AuthorityName name) {		
		return authorityService.getOne((new LambdaQueryWrapper<Authority>().eq(Authority::getName, name)));
	}

	@Transactional(rollbackFor = Exception.class)
	public Authority getOrCreateAuthorityByName(AuthorityName name) {
		Authority authority = this.findAuthorityByName(name);
		if (authority == null) {
			authority = new Authority();
			authority.setName(name);
			authority.setCreatedBy(JwtUser.currentUserId());
			this.save(authority);
		}		
		return authority;
	}
	
	public List<Authority> selectByUserId(Long userId) {
		return authorityMapper.selectByUserId(userId);
	}

	@Override
	public List<AuthorityName> selectAuthorityNameByUserId(Long userId) {
		List<Authority> authorities = this.selectByUserId(userId);
		return authorities.stream().map(authority -> authority.getName()).collect(Collectors.toList());		
	}

	@Override
	public List<User> selectUsersByAuthorityName(AuthorityName name) {
		Authority authority =  this.findAuthorityByName(name);		
		List<UserAuthority> userAuthorities = userAuthorityService.list(new LambdaQueryWrapperAdapter<UserAuthority>().eq(UserAuthority::getAuthorityId, authority.getId()));
		List<Long> userIds = userAuthorities.stream().map(ua -> ua.getUserId()).collect(Collectors.toList());				
		
		List<User> users = userIds.stream().map(userId -> {
			return userService.getUser(userId);
		}).collect(Collectors.toList());
		return users;
	}	
}

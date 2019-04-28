package com.bdease.spm.service.impl;

import com.bdease.spm.entity.Authority;
import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.mapper.AuthorityMapper;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IAuthorityService;
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
	
	
}

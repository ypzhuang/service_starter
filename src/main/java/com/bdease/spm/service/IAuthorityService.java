package com.bdease.spm.service;

import com.bdease.spm.entity.Authority;
import com.bdease.spm.entity.enums.AuthorityName;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
public interface IAuthorityService extends IService<Authority> {
	public Authority getOrCreateAuthorityByName(AuthorityName name);
	public List<Authority> selectByUserId(Long userId);
	public List<AuthorityName> selectAuthorityNameByUserId(Long userId);
}

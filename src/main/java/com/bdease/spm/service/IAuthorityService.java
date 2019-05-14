package com.bdease.spm.service;

import com.bdease.spm.entity.Authority;
import com.bdease.spm.entity.User;
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
	Authority getOrCreateAuthorityByName(AuthorityName name);

	List<Authority> selectByUserId(Long userId);

	List<AuthorityName> selectAuthorityNameByUserId(Long userId);

	List<User> selectUsersByAuthorityName(AuthorityName name);
}

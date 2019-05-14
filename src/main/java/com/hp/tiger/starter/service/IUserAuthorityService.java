package com.hp.tiger.starter.service;

import com.hp.tiger.starter.entity.Authority;
import com.hp.tiger.starter.entity.UserAuthority;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hp.tiger.starter.entity.enums.AuthorityName;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
public interface IUserAuthorityService extends IService<UserAuthority> {
    void removeUserAuthorityRelation(Long userId, AuthorityName name);

    void addUserAuthorityRelation(Long userId, List<Authority> authorities);
}

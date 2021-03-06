package com.hptiger.starter.service;

import com.hptiger.starter.entity.Authority;
import com.hptiger.starter.entity.UserAuthority;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hptiger.starter.entity.enums.AuthorityName;

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

package com.bdease.spm.service.impl;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.Authority;
import com.bdease.spm.entity.UserAuthority;
import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.mapper.UserAuthorityMapper;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IAuthorityService;
import com.bdease.spm.service.IUserAuthorityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
@Service
public class UserAuthorityServiceImpl extends ServiceImpl<UserAuthorityMapper, UserAuthority> implements IUserAuthorityService {

    @Autowired
    private IAuthorityService authorityService;

    @Transactional
    @Override
    public void removeUserAuthorityRelation(Long userId, AuthorityName name) {
        Authority authority = authorityService.getOrCreateAuthorityByName(name);
        this.remove(new LambdaQueryWrapperAdapter<UserAuthority>()
                .eq(UserAuthority::getUserId,userId)
                .eq(UserAuthority::getAuthorityId,authority.getId())
        );
    }

    @Transactional
    @Override
    public void addUserAuthorityRelation(Long userId, List<Authority> authorities) {
        List<UserAuthority> list = authorities.stream().map(auth -> {
            UserAuthority userAuthority = new UserAuthority();
            userAuthority.setAuthorityId(auth.getId());
            userAuthority.setUserId(userId);
            userAuthority.setCreatedBy(JwtUser.currentUserId());
            return userAuthority;
        }).collect(Collectors.toList());
        this.saveBatch(list);
    }
}

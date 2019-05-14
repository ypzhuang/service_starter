package com.hp.tiger.starter.service.impl;

import com.hp.tiger.starter.adapter.LambdaQueryWrapperAdapter;
import com.hp.tiger.starter.entity.Authority;
import com.hp.tiger.starter.entity.UserAuthority;
import com.hp.tiger.starter.entity.enums.AuthorityName;
import com.hp.tiger.starter.mapper.UserAuthorityMapper;
import com.hp.tiger.starter.security.JwtUser;
import com.hp.tiger.starter.service.IAuthorityService;
import com.hp.tiger.starter.service.IUserAuthorityService;
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

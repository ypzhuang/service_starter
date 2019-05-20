package com.hptiger.starter.service.impl;

import com.hptiger.starter.adapter.LambdaQueryWrapperAdapter;
import com.hptiger.starter.entity.Authority;
import com.hptiger.starter.entity.UserAuthority;
import com.hptiger.starter.entity.enums.AuthorityName;
import com.hptiger.starter.mapper.UserAuthorityMapper;
import com.hptiger.starter.security.JwtUser;
import com.hptiger.starter.service.IAuthorityService;
import com.hptiger.starter.service.IUserAuthorityService;
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

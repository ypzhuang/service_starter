package com.bdease.spm.service.impl;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.UserShop;
import com.bdease.spm.mapper.UserShopMapper;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IUserShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-14
 */
@Service
public class UserShopServiceImpl extends ServiceImpl<UserShopMapper, UserShop> implements IUserShopService {

    @Transactional
    @Override
    public void createUserShop(Long shopId, Long userId) {
        UserShop userShop = this.getOne(new LambdaQueryWrapperAdapter<UserShop>()
                .eq(UserShop::getShopId,shopId)
                .eq(UserShop::getUserId,userId)
        );
        if(userShop == null) {
            userShop = new UserShop();
            userShop.setShopId(shopId);
            userShop.setUserId(userId);
            userShop.setCreatedBy(JwtUser.currentUserId());
            this.save(userShop);
        }
    }

    @Transactional
    @Override
    public void createOrUpdateUserShop(Long shopId, List<Long> userIds) {
        for(Long userId : userIds) {
            List<UserShop> userShops = this.list(new LambdaQueryWrapperAdapter<UserShop>()
                    .eq(UserShop::getUserId,userId));
            boolean flag = false;
            List<Long> removeIds = new ArrayList<>();
            for(UserShop us : userShops) {
                if (us.getShopId().equals(shopId)) {
                    flag = true;
                } else {
                    removeIds.add(us.getId());
                }
            }
            if(removeIds.size() > 0) this.removeByIds(removeIds);
            if(!flag) {
                UserShop userShop = new UserShop();
                userShop.setShopId(shopId);
                userShop.setUserId(userId);
                userShop.setCreatedBy(JwtUser.currentUserId());
                this.save(userShop);
            }
        }
    }
}

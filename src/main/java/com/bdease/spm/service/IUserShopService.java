package com.bdease.spm.service;

import com.bdease.spm.entity.UserShop;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-14
 */
public interface IUserShopService extends IService<UserShop> {
    void createUserShop(Long shopId,Long userId);

    void createOrUpdateUserShop(Long shopId, List<Long> userIds);


}

package com.bdease.spm.service;
import com.bdease.spm.entity.MiniProgramUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bdease.spm.vo.GuestVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-02-02
 */
public interface IMiniProgramUserService extends IService<MiniProgramUser> {
	MiniProgramUser saveOrUpateMiniProgramUser(MiniProgramUser user);
	
	MiniProgramUser getMiniProgramUserByOpenId(String openId);

    MiniProgramUser updateMiniProgramUser(Long id, GuestVO guestVO);

	IPage<MiniProgramUser> getGuestsByPage(String user, Long shopId, Integer monthsOfNoPictures, Integer monthsOfNoOrders, Integer current, Integer size);
    
}

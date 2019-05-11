package com.bdease.spm.service.impl;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.Authority;
import com.bdease.spm.entity.MiniProgramUser;
import com.bdease.spm.entity.User;
import com.bdease.spm.entity.UserMiniProgramUser;
import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.mapper.MiniProgramUserMapper;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IAuthorityService;
import com.bdease.spm.service.IMiniProgramUserService;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserMiniProgramUserService;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.utils.DateHelper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.bdease.spm.vo.GuestVO;
import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-02-02
 */
@Service
public class MiniProgramUserServiceImpl extends ServiceImpl<MiniProgramUserMapper, MiniProgramUser> implements IMiniProgramUserService {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IUserMiniProgramUserService userMiniProgramUserService;
	
	@Autowired
	private IAuthorityService authorityService;	
	
	@Autowired
	private IShopService shopService;	
	
	@Value("${app.defaultPassword}")
	private String defaultPasswd;
	
	@Transactional
	@Override
	public MiniProgramUser saveOrUpateMiniProgramUser(MiniProgramUser user) {
		String openId = user.getOpenId();
		MiniProgramUser persistUser = this.getMiniProgramUserByOpenId(openId);
		if(persistUser == null) { //新增
			this.save(user);			
			User mockUser = new User();
			mockUser.setName(openId);
			mockUser.setUsername(openId);
			mockUser.setPassword(new BCryptPasswordEncoder().encode(openId + defaultPasswd)); 
			
			Authority userAuthority = authorityService.getOrCreateAuthorityByName(AuthorityName.ROLE_GUEST);			
			mockUser.setLastPasswordResetDate(LocalDateTime.now());
			mockUser.setAuthorities(Arrays.asList(userAuthority));		        
			mockUser.setEnabled(true);
			userService.saveUser(mockUser);
			
			Long miniProgramUserId = user.getId();
			Long userId = mockUser.getId();
			UserMiniProgramUser mapping = new UserMiniProgramUser();
			mapping.setMiniProgramUserId(miniProgramUserId);
			mapping.setUserId(userId);
			userMiniProgramUserService.save(mapping);			
		} else { // 更新
			user.setId(persistUser.getId());
			user.setUpdateTime(new Date());
			this.updateById(user);
		}
		return user;
	}

	@Override
	public MiniProgramUser getMiniProgramUserByOpenId(String openId) {		
		return this.getOne(new LambdaQueryWrapperAdapter<MiniProgramUser>().eq(MiniProgramUser::getOpenId, openId));
	}

	@Override
	public MiniProgramUser updateMiniProgramUser(Long id, GuestVO guestVO) {
		MiniProgramUser miniUser = this.getById(id);
		Asserts.check(miniUser != null, "不存在的客户ID：%d",id);

		MiniProgramUser miniProgramUser = new MiniProgramUser();
		BeanUtils.copyProperties(guestVO, miniProgramUser);
		miniProgramUser.setId(id);
		this.updateById(miniProgramUser);
		return this.getById(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IPage<MiniProgramUser> getGuestsByPage(String user, Long shopId, Integer monthsOfNoPictures, Integer monthsOfNoOrders, Integer current, Integer size) {
		Page<MiniProgramUser> page = new Page<>(current,size);
	    List<Long> shopIds = shopService.getOwnShopIds(JwtUser.currentUserId());
	
	    return page(page,new LambdaQueryWrapperAdapter<MiniProgramUser>()
	            .eq(MiniProgramUser::getShopId,shopId)
	            .nestedLike(user, MiniProgramUser::getNickName,MiniProgramUser::getPhone,MiniProgramUser::getName)
	            .isNullOrLe(MiniProgramUser::getLastPictureDate, DateHelper.getDate(monthsOfNoPictures))
	            .isNullOrLe(MiniProgramUser::getLastOrderDate, DateHelper.getDate(monthsOfNoOrders))
	            .in(MiniProgramUser::getShopId,shopIds)
	    );
	}

}

package com.hp.tiger.starter.service.impl;

import com.hp.tiger.starter.adapter.LambdaQueryWrapperAdapter;
import com.hp.tiger.starter.entity.App;
import com.hp.tiger.starter.mapper.AppMapper;
import com.hp.tiger.starter.security.JwtUser;
import com.hp.tiger.starter.service.IAppService;
import com.hp.tiger.starter.utils.IDHelper;
import com.hp.tiger.starter.vo.AppVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Third Part APP Management 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-15
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

	@Transactional
	@Override
	public App saveApp(AppVO appVO) {
		App app = new App();
		app.setOwnerEmail(appVO.getOwnerEmail());
		app.setAppId(IDHelper.generateUUID());
		app.setAppSecurity(IDHelper.generateUUID());
		app.setCreatedBy(JwtUser.currentUserId());
		this.save(app);
		return app;
	}
	
	@Transactional
	@Override
	public App mockApp() {
		App app = new App();		
		app.setAppId("415327502909408fa9634faec2b187f7");
		app.setAppSecurity("2561135a7f9643a8a1f7eb3ea8165631");	
		this.save(app);
		return app;
	}
	
	@Override
	public IPage<App> getAppsByPage(Integer current, Integer size) {
		Page<App> page = new Page<>(current,size);	
		return this.page(page);
	}

	@Override
	public App getApp(String appId, String security) {
		return this.getOne(new LambdaQueryWrapperAdapter<App>()
				.eq(App::getAppId, appId)
				.eq(App::getAppSecurity, security));	
	}
}

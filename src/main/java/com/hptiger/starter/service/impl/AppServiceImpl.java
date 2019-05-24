package com.hptiger.starter.service.impl;

import com.hptiger.starter.adapter.LambdaQueryWrapperAdapter;
import com.hptiger.starter.entity.App;
import com.hptiger.starter.entity.enums.Enable;
import com.hptiger.starter.mapper.AppMapper;
import com.hptiger.starter.security.JwtUser;
import com.hptiger.starter.service.IAppService;
import com.hptiger.starter.utils.IDHelper;
import com.hptiger.starter.vo.AppVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.http.util.Asserts;
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
	public IPage<App> getAppsByPage(String appId, Enable status, Integer current, Integer size) {
		Page<App> page = new Page<>(current,size);	
		return this.page(page, new LambdaQueryWrapperAdapter<App>()
				.eq(App::getAppId, appId)
				.eq(App::getStatus, status)
		);
	}

	@Override
	public App getApp(String appId, String security) {
		return this.getOne(new LambdaQueryWrapperAdapter<App>()
				.eq(App::getAppId, appId)
				.eq(App::getAppSecurity, security));	
	}

	@Transactional
	@Override
	public App switchStatus(Long id) {
		App app = findApp(id);

		App updateApp = new App();
		updateApp.setId(app.getId());
		updateApp.setStatus(Enable.YES.equals(app.getStatus()) ? Enable.NO : Enable.YES);
		this.updateById(updateApp);

		return findApp(id);
	}

	@Transactional
	@Override
	public App resetAppSecurity(Long id) {
		App app = findApp(id);

		App updateApp = new App();
		updateApp.setId(app.getId());
		updateApp.setAppSecurity(IDHelper.generateUUID());

		this.updateById(updateApp);

		return findApp(id);
	}

	@Transactional
	@Override
	public void deleteApp(Long id) {
		App app = findApp(id);
		this.removeById(app.getId());
	}

	@Override
	public App updateApp(Long id, AppVO appVO) {
		App app = findApp(id);

		App update = new App();
		update.setId(app.getId());
		update.setOwnerEmail(appVO.getOwnerEmail());

		this.updateById(update);
		return findApp(id);
	}

	private App findApp(Long id) {
		App app = this.getById(id);
		Asserts.check(app != null, "Illegal ID");
		return app;
	}
}

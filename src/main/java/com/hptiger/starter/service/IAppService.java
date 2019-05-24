package com.hptiger.starter.service;

import com.hptiger.starter.entity.App;
import com.hptiger.starter.entity.enums.Enable;
import com.hptiger.starter.vo.AppVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * Third Part APP Management 服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-15
 */
public interface IAppService extends IService<App> {

	App saveApp(AppVO appVO);
	
	App mockApp(); // only for test
	
	IPage<App> getAppsByPage(String appId, Enable status, Integer current, Integer size);

	App getApp(String appId, String security);

	App switchStatus(Long id);

	App resetAppSecurity(Long id);

	void deleteApp(Long id);

	App updateApp(Long id, AppVO appVO);
}

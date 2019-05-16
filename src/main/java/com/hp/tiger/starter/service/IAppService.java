package com.hp.tiger.starter.service;

import com.hp.tiger.starter.entity.App;
import com.hp.tiger.starter.vo.AppVO;
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
	
	IPage<App> getAppsByPage(Integer current, Integer size);

	App getApp(String appId, String security);
}

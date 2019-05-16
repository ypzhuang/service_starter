package com.hp.tiger.starter.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hp.tiger.starter.controller.BaseController;
import com.hp.tiger.starter.entity.App;
import com.hp.tiger.starter.service.IAppService;
import com.hp.tiger.starter.utils.IDHelper;
import com.hp.tiger.starter.vo.AppVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * Third Part APP Management Controller
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-15
 */
@RestController
@RequestMapping("/api/v1/apps")
@Api(tags = {"App"})
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER')")
public class AppController extends BaseController {
	@Autowired
	private IAppService appService;
	
	@GetMapping
	@ApiOperation(value = "Search all the apps by page")
	public IPage<App> getOrdersByPage(
			@ApiParam(value = "当前页", required = true, defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
			@ApiParam(value = "每页数量", required = true, defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size) {
		IPage<App> page = this.appService.getAppsByPage(current, size);
		page.getRecords().stream().forEach(app -> app.setAppSecurity(IDHelper.maskUUID(app.getAppSecurity())));
		return page;
	}
	
	@PostMapping
    @ApiOperation(value = "Create an App Id and App Security")
	public App createApp(@Valid @RequestBody AppVO appVO) {
		return appService.saveApp(appVO);
	}
}

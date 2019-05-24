package com.hptiger.starter.controller;

import javax.validation.Valid;

import com.hptiger.starter.entity.App;
import com.hptiger.starter.entity.enums.Enable;
import com.hptiger.starter.service.IAppService;
import com.hptiger.starter.utils.IDHelper;
import com.hptiger.starter.vo.AppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;

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
            @ApiParam(value = "App ID", required = false) @RequestParam(required = false) String appId,
            @ApiParam(value = "Status", required = false) @RequestParam(required = false) Enable status,
            @ApiParam(value = "当前页", required = true, defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量", required = true, defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size) {
        IPage<App> page = this.appService.getAppsByPage(appId, status, current, size);
        page.getRecords().stream().forEach(app -> app.setAppSecurity(IDHelper.maskUUID(app.getAppSecurity())));
        return page;
    }

    @PostMapping
    @ApiOperation(value = "Create an App Id and App Security")
    public App createApp(@Valid @RequestBody AppVO appVO) {
        return appService.saveApp(appVO);
    }

    @PutMapping("/{id}/switch")
    @ApiOperation(value = "Switch Status of APP ID")
    public App switchStatus(@PathVariable Long id) {
        App app = this.appService.switchStatus(id);
        app.setAppSecurity(IDHelper.maskUUID(app.getAppSecurity()));
        return app;
    }

    @PutMapping("/{id}/reset")
    @ApiOperation(value = "Reset App Security")
    public App resetAppSecurity(@PathVariable Long id) {
        App app =  this.appService.resetAppSecurity(id);
        return app;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete App ID")
    public void deleteAPP(@PathVariable Long id) {
        this.appService.deleteApp(id);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update Owner's Email")
    public App updateApp(@PathVariable Long id, @Valid @RequestBody AppVO appVO) {
        App app = this.appService.updateApp(id, appVO);
        app.setAppSecurity(IDHelper.maskUUID(app.getAppSecurity()));
        return app;
    }
}

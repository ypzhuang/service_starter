package com.bdease.spm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bdease.spm.entity.Photo;
import com.bdease.spm.service.IPhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 客户备注 前端控制器
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@RestController
@RequestMapping("/api/v1/photos")
@Api(tags = {"Photo"})
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER')")
//@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER','ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
public class PhotoController extends BaseController {
    @Autowired
    private IPhotoService photoService; 

    @GetMapping
    @ApiOperation(value = "分页查询护理照片")
    public IPage<Photo> getPhotosByPage(
            @ApiParam(value = "客户ID",required = true) @RequestParam(required = true) Long miniProgramUserId,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
        return this.photoService.getPhotosByPage(miniProgramUserId, current, size);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除照片")
    public void deletePhoto(@PathVariable Long id) {
        this.photoService.deletePhoto(id);
    }
}

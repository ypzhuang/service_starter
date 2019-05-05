package com.bdease.spm.controller.app.employee;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.entity.Photo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/app/emp/v1/photos")
@Api(tags={"MiniEmp"})
@PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
public class MiniPhotoController extends MiniBaseController{
	
	@GetMapping
    @ApiOperation(value = "分页查询护理照片")
    public IPage<Photo> getPhotosByPage(
            @ApiParam(value = "客户ID",required = true) @RequestParam(required = true) Long miniProgramUserId,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
		 return super.photoController.getPhotosByPage(miniProgramUserId, current, size);
    }
}

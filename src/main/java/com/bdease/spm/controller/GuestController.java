package com.bdease.spm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bdease.spm.entity.MiniProgramUser;
import com.bdease.spm.service.IMiniProgramUserService;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.vo.GuestVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/guests")
@Api(tags={"Guest"})
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER')")
//@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER','ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
public class GuestController extends BaseController {

    @Autowired
    private IMiniProgramUserService miniUserService;

    @Autowired
	public IShopService shopService;

	@GetMapping
    @ApiOperation(value = "分页查询客户")
    public IPage<MiniProgramUser> getGuestByPage(
            @ApiParam(value = "客户姓名、手机号或微信昵称",required = false) @RequestParam(required = false) String user,
            @ApiParam(value = "所属店铺",required = false) @RequestParam(required = false) Long shopId,
            @ApiParam(value = "N月未拍照", required = false) @RequestParam(required = false) Integer monthsOfNoPictures,
            @ApiParam(value = "N月没下单", required = false) @RequestParam(required = false) Integer monthsOfNoOrders,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
        return this.miniUserService.getGuestsByPage(user, shopId, monthsOfNoPictures, monthsOfNoOrders, current, size);
    }

	@PutMapping("/{id}")
    @ApiOperation(value = "更新客户")
    public MiniProgramUser updateGuest(@PathVariable Long id, @Valid @RequestBody GuestVO guestVO) {
        return this.miniUserService.updateMiniProgramUser(id,guestVO);
    }   
}

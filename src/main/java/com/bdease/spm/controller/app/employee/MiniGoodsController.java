package com.bdease.spm.controller.app.employee;


import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.controller.app.MiniBaseController;
import com.bdease.spm.entity.Goods;
import com.bdease.spm.entity.Shop;
import com.bdease.spm.entity.enums.GoodsStatus;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.vo.ShopGoodsVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping("/app/emp/v1/goods")
@Api(tags={"MiniEmp"})
public class MiniGoodsController extends MiniBaseController {	
	@Autowired
	private IUserService userService;
		
	@GetMapping
    @ApiOperation(value = "分页查询可销售的商品")
	// @PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
    public IPage<ShopGoodsVO> getGoodsByPage(            
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
		Shop shop = userService.getActiveShopOfCurrentUser();
		Asserts.check(shop != null, "当前用户:%d未设置当前店铺？",JwtUser.currentUserId());
		String goodsCondition = null;
		IPage<ShopGoodsVO>  pages = null;
		pages = super.shopGoodsController.getShopGoodsByPage(goodsCondition, shop.getId(), current, size);
		if(pages.getTotal() != 0) {
			return pages;
		} {
			IPage<Goods> goodsPages = super.goodsController.getGoodsByPage(goodsCondition, GoodsStatus.FOR_SALE, current, size);
			pages = new Page<ShopGoodsVO>();
			BeanUtils.copyProperties(goodsPages, pages);
			
			List<ShopGoodsVO> shopGoodsVOList = goodsPages.getRecords().stream().map(goods -> {
				ShopGoodsVO shopGoodsVO = new ShopGoodsVO();
				BeanUtils.copyProperties(goods,shopGoodsVO);
				shopGoodsVO.setShopPrice(shopGoodsVO.getPrice());
				return shopGoodsVO;
			}).collect(Collectors.toList());
			pages.setRecords(shopGoodsVOList);
			return pages;
		}		
    }
	
	
}

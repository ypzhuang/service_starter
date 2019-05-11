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
import com.bdease.spm.service.IGoodsService;
import com.bdease.spm.service.IShopGoodsService;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.vo.ShopGoodsVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping("/app/emp/v1/goods")
@Api(tags={"MiniEmp"})
@PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
public class MiniGoodsController extends MiniBaseController {	
	@Autowired
	private IUserService userService;
	
	@Autowired
    private IGoodsService goodsService;
	
	@Autowired
    private IShopService shopService;
	
	@Autowired
    private IShopGoodsService shopGoodsService;
		
	@GetMapping
    @ApiOperation(value = "分页查询可销售的商品")	
    public IPage<ShopGoodsVO> getGoodsByPage(            
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
		Shop shop = userService.getActiveShopOfCurrentUser();
		Asserts.check(shop != null, "当前用户:%d未设置当前店铺？",JwtUser.currentUserId());
		String goodsCondition = null;
		IPage<ShopGoodsVO>  pages = null;
			
		List<Long> shopIds = this.shopService.getOwnShopIds(JwtUser.currentUserId());        
		pages =  this.shopGoodsService.pageShopGoods(shop.getId(),goodsCondition,shopIds,current,size);
	        
		if(pages.getTotal() != 0) {
			return pages;
		} {
			IPage<Goods> goodsPages = this.goodsService.getGoodsByPage(goodsCondition, GoodsStatus.FOR_SALE, current, size);
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

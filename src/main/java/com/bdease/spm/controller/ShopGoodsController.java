package com.bdease.spm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.entity.Goods;
import com.bdease.spm.entity.Shop;
import com.bdease.spm.entity.ShopGoods;
import com.bdease.spm.entity.enums.ShopStatus;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IShopGoodsService;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.vo.ShopGoodsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 商品 前端控制器
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@RestController
@RequestMapping(value = "/api/v1/shopgoods")
@Api(tags = {"ShopGoods"})
public class ShopGoodsController extends BaseController {
    @Autowired
    private IShopGoodsService shopGoodsService;

    @Autowired
    private IShopService shopService;

    @GetMapping
    @ApiOperation(value = "分页查询店铺在售商品")
    public IPage<ShopGoodsVO> getShopGoodsByPage(
            @ApiParam(value = "商品名称或编号",required = false) @RequestParam(required = false) String goods,
            @ApiParam(value = "店铺ID",required = false) @RequestParam(required = false) Long shopId,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
        List<Long> shopIds = this.shopService.getOwnShopIds(JwtUser.currentUserId());
        Page<ShopGoodsVO> page = new Page<>(current,size);
        return this.shopGoodsService.pageShopGoods(page,shopId,goods,shopIds);
    }

    @GetMapping("/availableGoods")
    @ApiOperation(value = "分页查询某店铺尚未销售商品")
    public IPage<Goods> getAvailableGoodsForShopByPage(
            @ApiParam(value = "商品名称或编号",required = false) @RequestParam(required = false) String goods,
            @ApiParam(value = "店铺ID",required = true) @RequestParam(required = true) Long shopId,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
        List<Long> shopIds = this.shopService.getOwnShopIds(JwtUser.currentUserId());
        if(shopIds != null && !shopIds.isEmpty()) {
            Asserts.check(shopIds.contains(shopId),"用户：%d非法请求其他店铺数据:%d",JwtUser.currentUserId(),shopId);
        }
        Shop shop = this.shopService.getShop(shopId);
        Asserts.check(shop != null && shop.getStatus().equals(ShopStatus.OPEN),
                "不存在或歇业的店铺:%d不能添加商品",shopId);
        Page<Goods> page = new Page<>(current,size);
        return this.shopGoodsService.pageShopAvailableGoods(page,shopId,goods);
    }



    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除店铺在售商品")
    public void deleteShopGoods(@PathVariable Long id) {
        this.shopGoodsService.deleteShopGoods(id);
    }

    @PostMapping
    @ApiOperation(value = "新增店铺在售商品")
    public ShopGoods createShopGoods(
            @ApiParam(value = "店铺ID",required = true) @RequestParam(required = true) Long shopId,
            @ApiParam(value = "商品ID",required = true) @RequestParam(required = true) Long goodsId,
            @ApiParam(value = "店铺价格",required = true) @RequestParam(required = true) BigDecimal price
    ) {
        return this.shopGoodsService.saveShopGoods(shopId,goodsId,price);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "更新店铺在售商品价格")
    public ShopGoods updateShopGoodsById(
            @PathVariable Long id,
            @ApiParam(value = "店铺价格",required = true) @RequestParam(required = true) BigDecimal price
   ) {
        return this.shopGoodsService.updatePrice(id,price);
    }
}

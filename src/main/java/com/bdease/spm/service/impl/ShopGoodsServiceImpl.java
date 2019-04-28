package com.bdease.spm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.Goods;
import com.bdease.spm.entity.Shop;
import com.bdease.spm.entity.ShopGoods;
import com.bdease.spm.entity.enums.GoodsStatus;
import com.bdease.spm.mapper.ShopGoodsMapper;
import com.bdease.spm.service.IGoodsService;
import com.bdease.spm.service.IShopGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.vo.ShopGoodsVO;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Service
public class ShopGoodsServiceImpl extends ServiceImpl<ShopGoodsMapper, ShopGoods> implements IShopGoodsService {

    @Autowired
    private IShopService shopService;

    @Autowired
    private IGoodsService goodsService;

    @Override
    public void deleteShopGoods(Long id) {
        ShopGoods shopGoods = getShopGoods(id);
        this.removeById(shopGoods.getId());
    }

    @Override
    public ShopGoods updatePrice(Long id, BigDecimal price) {
        ShopGoods shopGoods = getShopGoods(id);
        shopGoods = new ShopGoods();
        shopGoods.setId(id);
        shopGoods.setPrice(price);
        this.updateById(shopGoods);
        return this.getShopGoods(id);
    }

    @Override
    public IPage<ShopGoodsVO> pageShopGoods(Page<ShopGoodsVO> page, Long shopId, String goodsName, List<Long> shopIds) {
        return this.baseMapper.pageShopGoods(page,shopId,goodsName,shopIds);
    }

    @Override
    public IPage<Goods> pageShopAvailableGoods(Page<Goods> page, Long shopId, String goodsName) {
        return this.baseMapper.pageShopAvailableGoods(page,shopId,goodsName);
    }

    @Override
    public ShopGoods saveShopGoods(Long shopId, Long goodsId, BigDecimal price) {
        Shop shop = this.shopService.getShop(shopId);
        Asserts.check(shop != null ,"不存在的店铺ID:%d", shopId);

        Goods good = this.goodsService.getGoods(goodsId);
        Asserts.check(good != null && good.getStatus().equals(GoodsStatus.FOR_SALE),"不存在或者暂售的商品ID:%d", goodsId);

        ShopGoods shopGoods = this.getOne(new LambdaQueryWrapperAdapter<ShopGoods>()
                .eq(ShopGoods::getShopId,shopId)
                .eq(ShopGoods::getGoodsId,goodsId)
        );
        Asserts.check(shopGoods == null ,"店铺ID：%d重复的在售商品ID:%d", shopId,goodsId);

        shopGoods = new ShopGoods();
        shopGoods.setGoodsId(goodsId);
        shopGoods.setShopId(shopId);
        shopGoods.setPrice(price);
        this.save(shopGoods);
        return getShopGoods(shopGoods.getId());
    }

    private ShopGoods getShopGoods(Long id) {
        ShopGoods shopGoods = this.getById(id);
        Asserts.check(shopGoods != null, "不存在的店铺在售商品ID：%d", id);
        return shopGoods;
    }
}

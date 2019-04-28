package com.bdease.spm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.entity.Goods;
import com.bdease.spm.entity.ShopGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bdease.spm.vo.ShopGoodsVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
public interface IShopGoodsService extends IService<ShopGoods> {

    void deleteShopGoods(Long id);

    ShopGoods updatePrice(Long id, BigDecimal price);

    IPage<ShopGoodsVO> pageShopGoods(Page<ShopGoodsVO> page, Long shopId, String goodsName, List<Long> shopIds);

    IPage<Goods> pageShopAvailableGoods(Page<Goods> page, Long shopId, String goodsName);

    ShopGoods saveShopGoods(Long shopId, Long goodsId, BigDecimal price);
}

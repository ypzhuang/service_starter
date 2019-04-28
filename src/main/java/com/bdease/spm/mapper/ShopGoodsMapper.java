package com.bdease.spm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.entity.Goods;
import com.bdease.spm.entity.ShopGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bdease.spm.vo.ShopGoodsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
public interface ShopGoodsMapper extends BaseMapper<ShopGoods> {
    IPage<ShopGoodsVO> pageShopGoods(Page<ShopGoodsVO> page,
                                     @Param("shopId") Long shopId,
                                     @Param("goodsName") String goodsName,
                                     @Param("shopIds")List<Long> shopIds
                                     );

    IPage<Goods> pageShopAvailableGoods(Page<Goods> page,
                                        @Param("shopId") Long shopId,
                                        @Param("goodsName") String goodsName);
}

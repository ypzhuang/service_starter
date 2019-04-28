package com.bdease.spm.service;

import com.bdease.spm.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bdease.spm.vo.GoodsVO;


/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
public interface IGoodsService extends IService<Goods> {
    void deleteGoods(Long id);

    Goods getGoods(Long id);

    Goods saveOrUpdateGoods(Long id, GoodsVO goodsVO);

}

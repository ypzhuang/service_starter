package com.bdease.spm.service.impl;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.Goods;
import com.bdease.spm.entity.enums.GoodsStatus;
import com.bdease.spm.mapper.GoodsMapper;
import com.bdease.spm.service.IGoodsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdease.spm.vo.GoodsVO;
import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Transactional
    @Override
    public void deleteGoods(Long id) {
        Goods goods = getGoods(id);
        this.removeById(goods.getId());
    }

    @Override
    public Goods getGoods(Long id) {
        Goods good = this.getById(id);
        Asserts.check(good != null, "不存在的商品id:%d",id);
        return good;
    }

    @Transactional
    @Override
    public Goods saveOrUpdateGoods(Long id, GoodsVO goodsVO) {
        checkGoodsRepetition(goodsVO.getName(),id);

        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsVO,goods);
        goods.setId(id);
        this.saveOrUpdate(goods);

        return this.getById(goods.getId());
    }

    private void checkGoodsRepetition( String name, Long forUpdateId) {
        Goods goods = this.getOne(new LambdaQueryWrapperAdapter<Goods>()
                .eq(Goods::getName, name));
        Asserts.check(goods == null || forUpdateId !=null && goods.getId().equals(forUpdateId) ,"重复的商品名称:%s", name);
    }
    
    @SuppressWarnings("unchecked")
	public IPage<Goods> getGoodsByPage(String goods, GoodsStatus status, Integer current, Integer size) {
		Page<Goods> page = new Page<>(current,size);
        return this.page(page, new LambdaQueryWrapperAdapter<Goods>()
                .eq(Goods::getStatus, status)
                .nestedLike(goods, Goods::getName,Goods::getIdentifier)
        );
	}
}

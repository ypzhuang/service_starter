package com.bdease.spm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.Goods;
import com.bdease.spm.entity.enums.GoodsStatus;
import com.bdease.spm.service.IGoodsService;

import com.bdease.spm.vo.GoodsVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * <p>
 * 商品 前端控制器
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@RestController
@RequestMapping(value = "/api/v1/goods")
@Api(tags = {"Goods"})
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER')")
public class GoodsController extends BaseController {
    @Autowired
    private IGoodsService goodsService;

    @SuppressWarnings("unchecked")
	@GetMapping
    @ApiOperation(value = "分页查询商品")
    public IPage<Goods> getGoodsByPage(
            @ApiParam(value = "商品名称或编号",required = false) @RequestParam(required = false) String goods,
            @ApiParam(value = "商品状态",required = false) @RequestParam(required = false) GoodsStatus status,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
        Page<Goods> page = new Page<>(current,size);
        return this.goodsService.page(page, new LambdaQueryWrapperAdapter<Goods>()
                .eq(Goods::getStatus, status)
                .nestedLike(goods, Goods::getName,Goods::getIdentifier)
        );
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除商品")
    public void deleteGoods(@PathVariable Long id) {
        this.goodsService.deleteGoods(id);
    }

    @PostMapping
    @ApiOperation(value = "新增商品")
    public Goods createGoods(@Valid @RequestBody GoodsVO goodsVO) {
        Goods goods =  this.goodsService.saveOrUpdateGoods(null, goodsVO);
        return goods;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "更新商品")
    public Goods updateGoodsById(@PathVariable Long id, @Valid @RequestBody GoodsVO goodsVO) {
        Goods pGoods = this.goodsService.getGoods(id);
        Asserts.check(pGoods != null, "不存在的商品ID：%d", id);

        Goods goods =  this.goodsService.saveOrUpdateGoods(id, goodsVO);
        return goods;
    }
}

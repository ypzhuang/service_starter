package com.bdease.spm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.Shop;

import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.vo.ShopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 店铺信息 前端控制器
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-14
 */
@RestController
@RequestMapping("/api/v1/shops")
@Api(tags = {"Shop"})
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER')")
//@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER','ROLE_SHOP_USER','ROLE_SHOP_ADMIN')")
public class ShopController extends BaseController {

    @Data
    @NoArgsConstructor
    class GEO {
        private BigDecimal latitude;
        private BigDecimal longitude;
    }

    @Autowired
    RestTemplate restTemplate;

    @Value("${app.geo.url}")
    private String geoURL;

    @Autowired
    private IShopService shopService;

    @SuppressWarnings("unchecked")
	@GetMapping
    @ApiOperation(value = "分页查询店铺")
    public IPage<Shop> getShopsByPage(
            @ApiParam(value = "省份代码",required = false) @RequestParam(required = false) String province,
            @ApiParam(value = "城市代码",required = false) @RequestParam(required = false) String city,
            @ApiParam(value = "店铺名称",required = false) @RequestParam(required = false) String name,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
        Page<Shop> page = new Page<>(current,size);
        List<Long> shopIds = this.shopService.getOwnShopIds(JwtUser.currentUserId());
        IPage<Shop> shops =  this.shopService.page(page, new LambdaQueryWrapperAdapter<Shop>()
                        .eq(Shop::getProvinceCode, province)
                        .eq(Shop::getCityCode, city)
                        .nestedLike(name, Shop::getName,Shop::getAddress)
                        .in(Shop::getId, shopIds)
        );
        shops.getRecords().forEach(shop -> this.shopService.addUserInformations(shop));
        return shops;
    }
    
	@GetMapping("/owns")
    @ApiOperation(value = "查询管辖的所有店铺,用于查询下拉条件")
    public List<Shop> getAllOwnShops() {        
        List<Long> shopIds = this.shopService.getOwnShopIds(JwtUser.currentUserId());      
        List<Shop> shops =  this.shopService.list(new LambdaQueryWrapperAdapter<Shop>().in(Shop::getId, shopIds));      
        return shops;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除店铺")
    public void deleteShop(@PathVariable Long id) {
        this.shopService.deleteShop(id);
    }

    @PostMapping
    @ApiOperation(value = "新增店铺")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public Shop createShop(@Valid @RequestBody ShopVO shopVO) {
        if((shopVO.getLatitude() == null || shopVO.getLongitude() == null) && shopVO.getAddress() != null) {
            try {
                String url = geoURL + shopVO.getAddress();
                log.debug("Request to: {}",url);
                GEO geo = restTemplate.getForObject(url,GEO.class);
                shopVO.setLatitude(geo.getLatitude());
                shopVO.setLongitude(geo.getLongitude());
            } catch(RestClientException e) {
                log.warn("get geo warnning:", e);
            }
        }
        Shop shop =  this.shopService.saveOrUpdateShop(null, shopVO);
        return shop;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "更新店铺")
    public Shop updateShopById(@PathVariable Long id, @Valid @RequestBody ShopVO shopVO) {
        Shop pShop = this.shopService.getShop(id);
        Asserts.check(pShop != null, "不存在的店铺ID：%d", id);

        Shop shop =  this.shopService.saveOrUpdateShop(id, shopVO);
        return shop;
    }


}

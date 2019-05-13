package com.bdease.spm;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.config.InitUserConfiguration;
import com.bdease.spm.entity.*;
import com.bdease.spm.entity.enums.*;
import com.bdease.spm.service.*;
import com.bdease.spm.vo.OrderItemVO;
import com.bdease.spm.vo.OrderVO;
import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class Bootstrap {
	private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);
	
    @PostConstruct
    public void postConstruct() {
    }

    @Autowired
    private IUserService userService;
    
    @Autowired
    private IAuthorityService authorityService;
    
    @Autowired
    private InitUserConfiguration initUserConfig;

	@Autowired
	private IShopService shopService;
	
	@Autowired
	private IUserShopService userShopService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private IShopGoodsService shopGoodsService;
	
	@Autowired
	private IOrderService orderService;

	@PostConstruct
	public void bootstrap() {		
		if(!isProd()) {
			initShops();
			initGoods();			
		}
		
		initUserAndRole();
		
		if(!isProd()) {
			initMiniUser();
			initOrders();
		}		
	}

	private void initShops() {
		log.info("bootstrap shops");
		Shop shop =  shopService.getOne(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getName, "金桥国际店"));
		if (shop == null) {
			shop = new Shop();
			shop.setAddress("金桥路1230号");
			shop.setStatus(ShopStatus.OPEN);
			shop.setName("金桥国际店");
			shopService.save(shop);
		}

		shop =  shopService.getOne(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getName, "金桥国际店2店"));
		if (shop == null) {
			shop = new Shop();
			shop.setAddress("金桥路1233号");
			shop.setStatus(ShopStatus.OPEN);
			shop.setName("金桥国际店2店");
			shopService.save(shop);
		}		
	}

	private void initGoods() {
		log.info("bootstrap goods");
		Goods goods = goodsService.getOne(new LambdaQueryWrapperAdapter<Goods>().eq(Goods::getName, "霸王洗发液"));
		if (goods == null) {
			goods = new Goods();
			goods.setName("霸王洗发液");
			goods.setIdentifier("A1001001");
			goods.setDescription("要不脱发，就用霸王");
			goods.setImgUrl("http://47.96.166.81:8888/group1/M00/00/00/rBAsLlxn0hOAH9tNAAFpvhEsD8I01.jpeg");
			goods.setPrice(new BigDecimal(50));
			goods.setStatus(GoodsStatus.OFFLINE);
			goods.setSpec("1000ml");
			goodsService.save(goods);
		}

		goods = goodsService.getOne(new LambdaQueryWrapperAdapter<Goods>().eq(Goods::getName, "海飞丝"));
		if (goods == null) {
			goods = new Goods();
			goods.setName("海飞丝");
			goods.setIdentifier("A1001002");
			goods.setDescription("要头发无头屑，就用海飞丝");
			goods.setImgUrl("http://47.96.166.81:8888/group1/M00/00/00/rBAsLlxn0hOAH9tNAAFpvhEsD8I01.jpeg");
			goods.setPrice(new BigDecimal(100));
			goods.setStatus(GoodsStatus.FOR_SALE);
			goods.setSpec("800ml");
			goodsService.save(goods);
		}		

		goods = goodsService.getOne(new LambdaQueryWrapperAdapter<Goods>().eq(Goods::getName, "飘柔"));		
		if (goods == null) {
			goods = new Goods();
			goods.setName("飘柔");
			goods.setIdentifier("A1001003");
			goods.setDescription("要头发飘逸，就用海飞丝");
			goods.setImgUrl("http://47.96.166.81:8888/group1/M00/00/00/rBAsLlxn0hOAH9tNAAFpvhEsD8I01.jpeg");
			goods.setPrice(new BigDecimal(150));
			goods.setStatus(GoodsStatus.FOR_SALE);
			goods.setSpec("500ml");
			goodsService.save(goods);
		}
		
		Commerce commerce = cnFaker.commerce();
		for(int i =0; i < 20; i++) {			
			String name = commerce.productName();
			goods = goodsService.getOne(new LambdaQueryWrapperAdapter<Goods>().eq(Goods::getName, name));		
			if (goods == null) {
				goods = new Goods();
				goods.setName(name);
				goods.setIdentifier(cnFaker.code().gtin8());
				goods.setDescription("要头发飘逸，就用海飞丝:" + commerce.toString());
				goods.setImgUrl("http://47.96.166.81:8888/group1/M00/00/00/rBAsLlxn0hOAH9tNAAFpvhEsD8I01.jpeg");
				goods.setPrice(new BigDecimal(cnFaker.random().nextInt(300)));
				goods.setStatus(GoodsStatus.FOR_SALE);
				goods.setSpec("500ml");
				goodsService.save(goods);
			}
		}

		Shop shop = shopService.getOne(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getName, "金桥国际店"));
		ShopGoods shopGoods = shopGoodsService.getOne(new LambdaQueryWrapperAdapter<ShopGoods>()
				.eq(ShopGoods::getId, shop.getId())
				.eq(ShopGoods::getGoodsId, goods.getId()));
		if (shopGoods == null) {
			shopGoods = new ShopGoods();		
			shopGoods.setShopId(shop.getId());
			shopGoods.setGoodsId(goods.getId());
			shopGoods.setPrice(new BigDecimal(149));
			shopGoodsService.save(shopGoods);
		}		
	}
	
	
	private void initOrders() {
		for (int i = 0; i < 22; i++) {
			OrderVO orderVO = new OrderVO();
			orderVO.setDiscountAmount(new BigDecimal(20));
			MiniProgramUser miniProgramUser = miniProgramUserService.getMiniProgramUserByOpenId(openIDs[i % 2]);
			orderVO.setMiniUserId(miniProgramUser.getId());
			orderVO.setPayAmount(new BigDecimal(cnFaker.number().numberBetween(300, 600)));
			Shop shop = shopService.getOne(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getName, "金桥国际店2店"));
			shopService.addUserInformations(shop);			
			orderVO.setShopId(shop.getId());
			log.debug("shop:{}",shop);
			
			Long userId = null;
			if(!shop.getShopUsers().isEmpty()) {
				userId = shop.getShopUsers().get(0);
			} else if(shop.getShopAdmin() != null){
				userId = shop.getShopAdmin();
			}	
			if(userId == null) break;
			
			orderVO.setSoldBy(userId);

			List<OrderItemVO> orderItems = new ArrayList<>();
			OrderItemVO item = null;
			Goods goods = null;
			item = new OrderItemVO();
			goods = goodsService.getOne(new LambdaQueryWrapperAdapter<Goods>().eq(Goods::getName, "飘柔"));
			item.setGoodsId(goods.getId());
			item.setPrice(new BigDecimal(150));
			item.setQuantity(2);
			orderItems.add(item);

			item = new OrderItemVO();
			goods = goodsService.getOne(new LambdaQueryWrapperAdapter<Goods>().eq(Goods::getName, "海飞丝"));
			item.setGoodsId(goods.getId());
			item.setPrice(new BigDecimal(100));
			item.setQuantity(3);
			orderItems.add(item);

			orderVO.setOrderItems(orderItems);
			orderService.createOrder(orderVO);
		}
	}


	private void initUserAndRole() {
		log.info("bootstrap user accounts");
		initUserConfig.getUsers().stream().forEach(u -> {
			log.info("init user: {}",u);
			Authority authority = authorityService.getOrCreateAuthorityByName(AuthorityName.getEnum(u.getRole()));		

			User user = userService.findUserByUsername(u.getUser());
			if (user == null) {
				user = new User();
				user.setUsername(u.getUser());
				user.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
				user.setName(u.getUser());
				user.setEmail(u.getUser()+"@sipimo.com");
				user.setEnabled(true);
				user.setLastPasswordResetDate(LocalDateTime.now());
				user.setAuthorities(Arrays.asList(authority));
				userService.saveUser(user);
				
				if(!isProd()) {
					if(authority.getName().equals(AuthorityName.ROLE_MANAGER)) {
						UserShop userShop = new UserShop();
						userShop.setUserId(user.getId());
						Shop shop = shopService.getOne(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getName, "金桥国际店"));
						userShop.setShopId(shop.getId());
						userShopService.save(userShop);
					}
					
					if(authority.getName().equals(AuthorityName.ROLE_SHOP_ADMIN)) {
						UserShop userShop = new UserShop();
						userShop.setUserId(user.getId());
						Shop shop = null;
						shop = shopService.getOne(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getName, "金桥国际店"));
						userShop.setShopId(shop.getId());
						userShopService.save(userShop);
						
						shop = shopService.getOne(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getName, "金桥国际店2店"));
						userShop.setShopId(shop.getId());
						userShopService.save(userShop);
					}
				}				
			}
		});		
	}
	
	
    @Autowired
    private Environment environment;
    
	public boolean isProd() {
		return Arrays.stream(environment.getActiveProfiles()).anyMatch("prod"::equals);
	}
	
	@Autowired
	private IMiniProgramUserService miniProgramUserService;
	
	Faker cnFaker = new Faker(new Locale("zh-CN"));

	private static final String []openIDs = new String[] {"olOXT5N6iMh6Pvg8gIaFoulqK6L0","olOXT5FPGvMpSZjWYhlqTiRM3-01",
			"olOXT5FPGvMpSZjWYhlqTiRM3-02","olOXT5FPGvMpSZjWYhlqTiRM3-03","olOXT5FPGvMpSZjWYhlqTiRM3-04",
			"olOXT5FPGvMpSZjWYhlqTiRM3-05","olOXT5N6iMh6Pvg8gIaFoulqK-06","olOXT5FPGvMpSZjWYhlqTiRM3-7",
			"olOXT5N6iMh6Pvg8gIaFoulq-8","olOXT5FPGvMpSZjWYhlqTiRM3-9","olOXT5FPGvMpSZjWYhlqTiRM3-10","olOXT5FPGvMpSZjWYhlqTiRM3-11"};
	private void initMiniUser() {
		Shop shop = shopService.getOne(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getName, "金桥国际店2店"));
		Arrays.stream(openIDs).forEach(openID -> {
			MiniProgramUser miniProgramUser = new MiniProgramUser();
			miniProgramUser.setName(cnFaker.name().name());
			miniProgramUser.setOpenId(openID);
			miniProgramUser.setShopId(shop.getId());
			miniProgramUserService.saveOrUpateMiniProgramUser(miniProgramUser);	
		});
	}
}

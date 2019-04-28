package com.bdease.spm;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.config.InitUserConfiguration;
import com.bdease.spm.entity.*;
import com.bdease.spm.entity.enums.*;
import com.bdease.spm.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

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

	@PostConstruct
	public void bootstrap() {		
		if(!isProd()) {
			initShops();
			initGoods();
		}
		
		initUserAndRole();
		
		if(!isProd()) {
			initMiniUser();
		}		
	}

	private void initShops() {
		log.info("bootstrap shops");
		Shop shop = null;
		shop = new Shop();
		shop.setAddress("金桥路1230号");
		shop.setStatus(ShopStatus.OPEN);
		shop.setName("金桥国际店");
		shopService.save(shop);

		shop = new Shop();
		shop.setAddress("金桥路1233号");
		shop.setStatus(ShopStatus.OPEN);
		shop.setName("金桥国际店2店");
		shopService.save(shop);
	}

	private void initGoods() {
		log.info("bootstrap goods");
		Goods goods = null;

		goods = new Goods();
		goods.setName("霸王洗发液");
		goods.setIdentifier("A1001001");
		goods.setDescription("要不脱发，就用霸王");
		goods.setImgUrl("http://47.96.166.81:8888/group1/M00/00/00/rBAsLlxn0hOAH9tNAAFpvhEsD8I01.jpeg");
		goods.setPrice(new BigDecimal(50));
		goods.setStatus(GoodsStatus.OFFLINE);
		goods.setSpec("1000ml");
		goodsService.save(goods);

		goods = new Goods();
		goods.setName("海飞丝");
		goods.setIdentifier("A1001002");
		goods.setDescription("要头发无头屑，就用海飞丝");
		goods.setImgUrl("http://47.96.166.81:8888/group1/M00/00/00/rBAsLlxn0hOAH9tNAAFpvhEsD8I01.jpeg");
		goods.setPrice(new BigDecimal(100));
		goods.setStatus(GoodsStatus.FOR_SALE);
		goods.setSpec("800ml");
		goodsService.save(goods);

		goods = new Goods();
		goods.setName("飘柔");
		goods.setIdentifier("A1001003");
		goods.setDescription("要头发飘逸，就用海飞丝");
		goods.setImgUrl("http://47.96.166.81:8888/group1/M00/00/00/rBAsLlxn0hOAH9tNAAFpvhEsD8I01.jpeg");
		goods.setPrice(new BigDecimal(150));
		goods.setStatus(GoodsStatus.FOR_SALE);
		goods.setSpec("500ml");
		goodsService.save(goods);

		ShopGoods shopGoods = new ShopGoods();
		Shop shop = shopService.getOne(new LambdaQueryWrapperAdapter<Shop>().eq(Shop::getName, "金桥国际店"));
		shopGoods.setShopId(shop.getId());
		shopGoods.setGoodsId(goods.getId());
		shopGoods.setPrice(new BigDecimal(149));
		shopGoodsService.save(shopGoods);
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
					if(authority.getName().equals(AuthorityName.ROLE_SHOP_MANAGER)) {
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
	

	private static final String []openIDs = new String[] {"olOXT5N6iMh6Pvg8gIaFoulqK6L0","olOXT5FPGvMpSZjWYhlqTiRM3-2o"};
	private void initMiniUser() {		
		Arrays.stream(openIDs).forEach(openID -> {
			MiniProgramUser miniProgramUser = new MiniProgramUser();
			miniProgramUser.setOpenId(openID);		
			miniProgramUserService.saveOrUpateMiniProgramUser(miniProgramUser);	
		});
	}
}

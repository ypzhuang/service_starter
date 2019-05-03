package com.bdease.spm.controller.app;

import com.bdease.spm.controller.AuthController;
import com.bdease.spm.controller.BaseController;
import com.bdease.spm.controller.DictionaryController;
import com.bdease.spm.controller.GoodsController;
import com.bdease.spm.controller.GuestController;
import com.bdease.spm.controller.OrderController;
import com.bdease.spm.controller.PhotoController;
import com.bdease.spm.controller.ShopGoodsController;

import org.springframework.beans.factory.annotation.Autowired;


public class MiniBaseController extends BaseController {	
    @Autowired
    protected DictionaryController dictionaryController;
    
    @Autowired
    protected GuestController guestController;
    
    @Autowired
    protected PhotoController photoController;
    
    @Autowired
    protected AuthController authController;
    
    @Autowired
    protected OrderController orderController;
    
    @Autowired
    protected ShopGoodsController shopGoodsController;
    
    @Autowired
    protected GoodsController goodsController;
}

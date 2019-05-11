package com.bdease.spm.controller.app;

import com.bdease.spm.controller.AuthController;
import com.bdease.spm.controller.BaseController;
import com.bdease.spm.controller.DictionaryController;
import com.bdease.spm.controller.OrderController;
import com.bdease.spm.controller.PhotoController;


import org.springframework.beans.factory.annotation.Autowired;


public class MiniBaseController extends BaseController {	
    @Autowired
    protected DictionaryController dictionaryController;    
    
    @Autowired
    protected PhotoController photoController;
    
    @Autowired
    protected AuthController authController;
    
    @Autowired
    protected OrderController orderController;   
   
}

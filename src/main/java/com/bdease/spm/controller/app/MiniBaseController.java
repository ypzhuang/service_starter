package com.bdease.spm.controller.app;

import com.bdease.spm.controller.AuthController;
import com.bdease.spm.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;


public class MiniBaseController extends BaseController {   
    
    @Autowired
    protected AuthController authController;        
   
}

package com.bdease.spm.controller.app;

import com.bdease.spm.controller.BaseController;
import com.bdease.spm.controller.DictionaryController;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

@Api(tags={"Mini"},description="Mini端API列表")
public class MiniBaseController extends BaseController {
	


    @Autowired
    protected DictionaryController dictionaryController;
    

}

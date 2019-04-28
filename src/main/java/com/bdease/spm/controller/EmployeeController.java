package com.bdease.spm.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.entity.Shop;
import com.bdease.spm.entity.User;
import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.service.IAuthorityService;
import com.bdease.spm.service.IShopService;
import com.bdease.spm.service.IUserService;
import com.bdease.spm.vo.UserVO;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.Match;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.validation.Valid;

import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/employees")
@Api(tags= {"Employee"} )
public class EmployeeController extends BaseController{

    @Autowired
    private IUserService service;
    
    @Autowired
    private IShopService shopService;
    
    @Autowired
    private IAuthorityService authorityService;

    @GetMapping
    @ApiOperation(value = "分页查询员工")
    public IPage<User> getUsersByPage(
            @ApiParam(value = "姓名或者手机号",required = false) @RequestParam(required = false) String nameOrUserName,
            @ApiParam(value = "店铺ID",required = false) @RequestParam(required = false) Long shopId,
            @ApiParam(value = "职位",required = false) @RequestParam(required = false) AuthorityName role,
            @ApiParam(value = "状态", required = false) @RequestParam(required = false) Boolean status,
            @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
            @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
    ) {
        Page<User> page = new Page<>(current,size);
        IPage<User> users =  this.service.pageUsers(page, nameOrUserName, shopId, role, status);
        users.getRecords().forEach(user -> {
        	user.setShops(shopService.getShopsByUserId(user.getId()));
        	user.setAuthorityNames(authorityService.selectAuthorityNameByUserId(user.getId()));
        });
        return super.json.use(JsonView.with(users)
		        .onClass(User.class, Match.match().exclude(UserExcludeProperties))
		        .onClass(Shop.class, Match.match().exclude("*").include(ShopIncludeProperties))
		       ).returnValue();       
    }
    
    static final String[] UserExcludeProperties = new String[] {
			"password"
	};
    static final String[] ShopIncludeProperties = new String[] {
			"id","name"
	};
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除员工")
    public void deleteUser(@PathVariable Long id) {
    	this.service.deleteUser(id);
    }
    
    @PostMapping
	@ApiOperation(value = "新增员工")
	public User createUser(@Valid @RequestBody UserVO userVO) {
    	User user =  service.saveOrUpdateUser(null, userVO);
    	return super.json.use(JsonView.with(user)
 		        .onClass(User.class, Match.match().exclude(UserExcludeProperties))
 		        .onClass(Shop.class, Match.match().exclude("*").include(ShopIncludeProperties))
 		       ).returnValue();    
	}
    
    @PutMapping("/{id}")
	@ApiOperation(value = "更新员工")
	public User updatePlotById(@PathVariable Long id, @Valid @RequestBody UserVO userVO) {
		User pUser = this.service.getById(id);
		Asserts.check(pUser != null, "不存在的用户ID：%d", id);
		
		User user =  service.saveOrUpdateUser(id, userVO);
    	return super.json.use(JsonView.with(user)
 		        .onClass(User.class, Match.match().exclude(UserExcludeProperties))
 		        .onClass(Shop.class, Match.match().exclude("*").include(ShopIncludeProperties))
 		       ).returnValue(); 
	}
    
    @PutMapping("/reset/{id}")
   	@ApiOperation(value = "重置密码")
   	public void resetPassword(@PathVariable Long id) {   		
   		service.resetPassword(id);   		
   	}    
}

package com.bdease.spm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdease.spm.entity.enums.AuthorityName;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IAuthorityService;
import com.bdease.spm.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/v1/authorities")
@Api(tags = { "Authority" })
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER')")
public class AuthorityController extends BaseController {
	
	@Autowired
	private IUserService service;
	
	@Autowired
	private IAuthorityService authorityService;
	
	@GetMapping
	@ApiOperation(value = "职位列表")
	public List<Map<String, String>> getAuthorities() {
		List<Map<String, String>> results = new ArrayList<>();
		for (AuthorityName authorityName : AuthorityName.values()) {
			if (authorityName.equals(AuthorityName.ROLE_MANAGER) || authorityName.equals(AuthorityName.ROLE_SHOP_ADMIN)
					|| authorityName.equals(AuthorityName.ROLE_SHOP_USER)) {
				Map<String, String> e = new HashMap<>();
				e.put(authorityName.getValue(), authorityName.getDesc());
				results.add(e);
			}
		}
		return results;
	}
	
	@GetMapping("/filter")
	@ApiOperation(value = "根据角色过滤的职位")
	public List<Map<String, String>> getFilterAuthorities() {
		List<Map<String, String>> results = new ArrayList<>();
		List<AuthorityName> authorities = authorityService.selectAuthorityNameByUserId(JwtUser.currentUserId());
		for (AuthorityName authorityName : AuthorityName.values()) {
			if (authorityName.equals(AuthorityName.ROLE_MANAGER) && authorities.contains(AuthorityName.ROLE_SUPER_ADMIN) || authorityName.equals(AuthorityName.ROLE_SHOP_ADMIN)
					|| authorityName.equals(AuthorityName.ROLE_SHOP_USER)) {
				Map<String, String> e = new HashMap<>();
				e.put(authorityName.getValue(), authorityName.getDesc());
				results.add(e);
			}
		}
		return results;
	}
}

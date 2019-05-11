package com.bdease.spm.controller.app;

import com.bdease.spm.service.IDictionaryService;
import com.bdease.spm.vo.DictCodeVO;
import com.bdease.spm.vo.DictTypeVO;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.Match;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/v1/dicts")
@Api(tags={"MiniEmp","MiniGuest"})
@PreAuthorize("hasAnyRole('ROLE_SHOP_USER','ROLE_SHOP_ADMIN','ROLE_GUEST')")
public class MiniDictionaryController extends MiniBaseController {
	@Autowired
	private IDictionaryService dictionaryService;
	
	private static final String[] DictTypeVOExcludedProperties = new String[] {
			"module"
	};	
	private static final String[] DictCodeVOExcludedProperties = new String[] {
			"id","remark"
	};
	
    @GetMapping
    @ApiOperation(value = "查询所有数据字典")
    public List<DictTypeVO> getAllDictionaries() {
    	List<DictTypeVO> list =  this.dictionaryService.getAllDicts();
        return super.json.use(JsonView.with(list)
		        .onClass(DictTypeVO.class, Match.match()
		            .exclude(DictTypeVOExcludedProperties))
		        .onClass(DictCodeVO.class, Match.match()
			            .exclude(DictCodeVOExcludedProperties))
		       ).returnValue();
    }
}

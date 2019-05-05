package com.bdease.spm.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdease.spm.entity.Dictionary;
import com.bdease.spm.entity.enums.Enable;
import com.bdease.spm.service.IDictionaryService;
import com.bdease.spm.vo.DictCodeVO;
import com.bdease.spm.vo.DictTypeVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author John Zhuang
 * @since 2019-01-03
 */
@RestController
@RequestMapping("/api/v1/dicts")
@Api(tags = {"Dict"})
@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MANAGER')")
public class DictionaryController extends BaseController {
	
	@Autowired
    protected IDictionaryService service;
   
    @GetMapping
    @ApiOperation(value = "查询所有数据字典")
    public List<DictTypeVO> getAllDictionaries() {
    	List<DictTypeVO> list = service.selectGroupByTypeCode();
    	for(DictTypeVO dictType: list) {
    		List<DictCodeVO> codes = service.selectByTypeCode(dictType.getTypeCode());
    		dictType.setCodes(codes);
    	}
    	return list;     
    }
    
    @GetMapping("/pages")
    @ApiOperation(value = "分页查询所有数据字典")
    public IPage<Dictionary> getAllDictionariesByPage(
		   @ApiParam(value = "当前页",required = true,defaultValue = "1") @RequestParam(required = true, defaultValue = "1") Integer current,
		   @ApiParam(value = "每页数量",required = true,defaultValue = "10") @RequestParam(required = true, defaultValue = "10") Integer size
		   ) {
    	Page<Dictionary> page = new Page<Dictionary>(current,size);  
    	return service.page(page);
    }

    @PostMapping(value = "/{typeCode}")
    @ApiOperation(value = "新建指定类别下的数字字典")
    public Dictionary createDictUnderCodeType(@PathVariable String typeCode,@Valid @RequestBody DictCodeVO dictCodeVO) {    	
    	Optional<DictTypeVO> dictType =  service.selectGroupByTypeCode().stream().filter(t -> typeCode.equals(t.getTypeCode())).findFirst();    	
    	Asserts.check(dictType.isPresent(), "typeCode：%s 不存在",typeCode);
    	Dictionary dict = new Dictionary();
    	BeanUtils.copyProperties(dictType.get(), dict);
    	BeanUtils.copyProperties(dictCodeVO, dict);
    	dict.setEnable(Enable.YES);
    	service.save(dict);
    	return dict;
    }
    
    @PutMapping(value = "/{id}")
    @ApiOperation(value = "更新数字字典")
    public Dictionary updateDictById(@PathVariable Long id,@Valid @RequestBody DictCodeVO dictCodeVO) {
    	Asserts.check(service.getById(id) != null, "id：%d 不存在",id);

    	Dictionary dict = new Dictionary();
    	dict.setId(id);
    	//只需更新以下字段
    	dict.setName(dictCodeVO.getName());
    	dict.setEnable(dictCodeVO.getEnable());
    	dict.setRemark(dictCodeVO.getRemark());    	
    	
    	service.updateById(dict);
    	return service.getById(id);
    }
}

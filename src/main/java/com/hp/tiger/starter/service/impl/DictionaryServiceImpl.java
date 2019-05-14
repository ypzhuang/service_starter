package com.hp.tiger.starter.service.impl;

import com.hp.tiger.starter.entity.Dictionary;
import com.hp.tiger.starter.mapper.DictionaryMapper;
import com.hp.tiger.starter.service.IDictionaryService;
import com.hp.tiger.starter.vo.DictCodeVO;
import com.hp.tiger.starter.vo.DictTypeVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-01-03
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {

	@Override
	public List<DictTypeVO> selectGroupByTypeCode() {
		return this.baseMapper.selectGroupByTypeCode();
	}
	
	@Override
	public List<DictCodeVO> selectByTypeCode(String typeCode) {
		return baseMapper.selectByTypeCode(typeCode);
	}
	
	@Override
	public List<DictTypeVO> getAllDicts() {
		List<DictTypeVO> list = this.selectGroupByTypeCode();
    	for(DictTypeVO dictType: list) {
    		List<DictCodeVO> codes = this.selectByTypeCode(dictType.getTypeCode());
    		dictType.setCodes(codes);
    	}
    	return list;
	}
}

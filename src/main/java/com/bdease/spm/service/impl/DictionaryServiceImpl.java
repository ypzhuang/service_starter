package com.bdease.spm.service.impl;

import com.bdease.spm.entity.Dictionary;
import com.bdease.spm.mapper.DictionaryMapper;
import com.bdease.spm.service.IDictionaryService;
import com.bdease.spm.vo.DictCodeVO;
import com.bdease.spm.vo.DictTypeVO;
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

	public List<DictTypeVO> selectGroupByTypeCode() {
		return this.baseMapper.selectGroupByTypeCode();
	}
	
	public List<DictCodeVO> selectByTypeCode(String typeCode) {
		return baseMapper.selectByTypeCode(typeCode);
	}
}

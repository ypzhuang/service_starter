package com.bdease.spm.service;

import com.bdease.spm.entity.Dictionary;
import com.bdease.spm.vo.DictCodeVO;
import com.bdease.spm.vo.DictTypeVO;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-01-03
 */
public interface IDictionaryService extends IService<Dictionary> {
	List<DictTypeVO> selectGroupByTypeCode();
	
	List<DictCodeVO> selectByTypeCode(String typeCode);
	
	List<DictTypeVO> getAllDicts();
}

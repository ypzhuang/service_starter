package com.hp.tiger.starter.service;

import com.hp.tiger.starter.entity.Dictionary;
import com.hp.tiger.starter.vo.DictCodeVO;
import com.hp.tiger.starter.vo.DictTypeVO;

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

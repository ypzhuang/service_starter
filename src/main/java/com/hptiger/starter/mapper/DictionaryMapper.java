package com.hptiger.starter.mapper;


import com.hptiger.starter.entity.Dictionary;
import com.hptiger.starter.vo.DictCodeVO;
import com.hptiger.starter.vo.DictTypeVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author John Zhuang
 * @since 2019-01-03
 */
public interface DictionaryMapper extends BaseMapper<Dictionary> {
	List<DictTypeVO> selectGroupByTypeCode();
	
	List<DictCodeVO> selectByTypeCode(@Param("typeCode") String typeCode);
}

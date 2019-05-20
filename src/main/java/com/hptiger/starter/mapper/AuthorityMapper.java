package com.hptiger.starter.mapper;

import com.hptiger.starter.entity.Authority;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
public interface AuthorityMapper extends BaseMapper<Authority> {
	List<Authority> selectByUserId(@Param("userId") Long userId);
}

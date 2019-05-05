package com.bdease.spm.mapper;

import com.bdease.spm.entity.User;
import com.bdease.spm.entity.enums.AuthorityName;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author John Zhuang
 * @since 2018-12-29
 */
public interface UserMapper extends BaseMapper<User> {
	User getUserByOpenId(@Param("openId") String openId);	

	IPage<User> pageUsers(Page<User> page, 
			@Param("nameOrUserName") String nameOrUserName, 
			@Param("shopId") Long shopId, 
			@Param("role") AuthorityName role,
			@Param("status") Boolean status,
			@Param("shopIds") List<Long> shopIds);

	List<User> findUsers(@Param("shopId") Long shopId,@Param("role") AuthorityName role);
}

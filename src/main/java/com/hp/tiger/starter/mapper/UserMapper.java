package com.hp.tiger.starter.mapper;

import com.hp.tiger.starter.entity.User;
import com.hp.tiger.starter.entity.enums.AuthorityName;
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
}

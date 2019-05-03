package com.bdease.spm.mapper;

import com.bdease.spm.entity.Photo;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 客户备注 Mapper 接口
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
public interface PhotoMapper extends BaseMapper<Photo> {
	@Select("SELECT max(taked_date) as last_picture_date FROM photo where del_flag = '0' and mini_user_id = #{guestId}")
	LocalDate getLatestTakedPhotoDate(Long guestId);
}

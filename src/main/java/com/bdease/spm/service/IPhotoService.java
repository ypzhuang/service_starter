package com.bdease.spm.service;

import com.bdease.spm.entity.Photo;

import java.time.LocalDate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户备注 服务类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
public interface IPhotoService extends IService<Photo> {

    void deletePhoto(Long id);
    
    LocalDate getLatestTakedPhotoDate(Long guestId);
    
    IPage<Photo> getPhotosByPage(Long miniProgramUserId, Integer current, Integer size);
}

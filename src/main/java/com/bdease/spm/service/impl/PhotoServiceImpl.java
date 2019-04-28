package com.bdease.spm.service.impl;

import com.bdease.spm.entity.Photo;
import com.bdease.spm.mapper.PhotoMapper;
import com.bdease.spm.service.IPhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.util.Asserts;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户备注 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-04-15
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements IPhotoService {

    @Override
    public void deletePhoto(Long id) {
        Photo photo = getPhoto(id);
        this.removeById(photo.getId());
    }

    private Photo getPhoto(Long id) {
        Photo photo = this.getById(id);
        Asserts.check(photo != null, "不存在的照片ID:%d",id);
        return photo;
    }
}

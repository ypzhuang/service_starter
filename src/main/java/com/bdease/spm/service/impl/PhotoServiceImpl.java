package com.bdease.spm.service.impl;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.Photo;
import com.bdease.spm.mapper.PhotoMapper;
import com.bdease.spm.security.JwtUser;
import com.bdease.spm.service.IPhotoService;
import com.bdease.spm.service.IShopService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.util.List;

import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private IShopService shopService;
    
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

	@Override
	public LocalDate getLatestTakedPhotoDate(Long guestId) {		
		return this.baseMapper.getLatestTakedPhotoDate(guestId);
	}
	
	@Override
	public IPage<Photo> getPhotosByPage(Long miniProgramUserId, Integer current, Integer size) {
		Page<Photo> page = new Page<>(current,size);
        List<Long> shopIds = this.shopService.getOwnShopIds(JwtUser.currentUserId());
        return this.page(page,new LambdaQueryWrapperAdapter<Photo>()
                .eq(Photo::getMiniUserId,miniProgramUserId)
                .in(Photo::getShopId, shopIds)
        );
	}
}

package com.bdease.spm.scheduler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bdease.spm.adapter.LambdaQueryWrapperAdapter;
import com.bdease.spm.entity.MiniProgramUser;
import com.bdease.spm.service.IMiniProgramUserService;
import com.bdease.spm.service.IOrderService;
import com.bdease.spm.service.IPhotoService;

@Component
public class Scheduler {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IMiniProgramUserService miniProgramUserService;
	
	@Autowired
	private IPhotoService photoService;
	
	@Scheduled(cron = "0 0/5 * * * ?")
	public void updateOrderStaticsForUsers() {
		log.debug("Scheduler of Update Order Statics for Users started.");
		
		
		List<Map<String,Object>> counts = orderService.countGroupByGuestId();
		for(Map<String,Object> countMap: counts) {
			Long count = (Long)countMap.get("count");
			Long guestId = (Long)countMap.get("mini_user_id");			
			log.debug("Guest Id:{},count:{} ",guestId,count);
			
			MiniProgramUser guest = new MiniProgramUser();
			guest.setId(guestId);			
			guest.setOrderCount(count);
			guest.setLastOrderDate(orderService.getLatestOrderDate(guest.getId()));
			guest.setLastPictureDate(photoService.getLatestTakedPhotoDate(guest.getId()));
			
			MiniProgramUser pMiniProgramUser = miniProgramUserService.getOne(new LambdaQueryWrapperAdapter<MiniProgramUser>()
					.eq(MiniProgramUser::getId, guest.getId())
					.eq(MiniProgramUser::getOrderCount, count)
					.eq(MiniProgramUser::getLastOrderDate, guest.getLastOrderDate())
					.eq(MiniProgramUser::getLastPictureDate, guest.getLastPictureDate()));
			if(pMiniProgramUser == null) {				
				miniProgramUserService.updateById(guest);
			}			
		}
	}
}

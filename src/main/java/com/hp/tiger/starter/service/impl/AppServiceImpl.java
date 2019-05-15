package com.hp.tiger.starter.service.impl;

import com.hp.tiger.starter.entity.App;
import com.hp.tiger.starter.mapper.AppMapper;
import com.hp.tiger.starter.service.IAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Third Part APP Management 服务实现类
 * </p>
 *
 * @author John Zhuang
 * @since 2019-05-15
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

}

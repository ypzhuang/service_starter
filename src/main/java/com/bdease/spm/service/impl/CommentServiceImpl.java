package com.bdease.spm.service.impl;

import com.bdease.spm.entity.Comment;
import com.bdease.spm.mapper.CommentMapper;
import com.bdease.spm.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}

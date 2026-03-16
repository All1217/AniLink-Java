package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Comment;
import com.video.web.main.mapper.CommentMapper;
import com.video.web.main.service.CommentService;
import com.video.web.main.vo.CommentQueryVo;
import com.video.web.main.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public IPage<CommentVo> pageRootItem(IPage<CommentVo> page, CommentQueryVo queryVo) {
        return commentMapper.pageRootItem(page, queryVo);
    }

    @Override
    public Long insertOne(Comment comment) {
        commentMapper.insert(comment);
        return comment.getId();
    }

    @Override
    public IPage<CommentVo> pageReplyItem(IPage<CommentVo> page, CommentQueryVo queryVo) {
        return commentMapper.pageReplyItem(page, queryVo);
    }

    @Override
    public void logicDelete(CommentQueryVo queryVo) {
        Comment t = new Comment();
        t.setIsDeleted(1);
        UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", queryVo.getId());
        updateWrapper.eq("vid", queryVo.getVid());
        commentMapper.update(t, updateWrapper);
    }
}

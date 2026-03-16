package com.video.web.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Comment;
import com.video.web.main.vo.CommentQueryVo;
import com.video.web.main.vo.CommentVo;

public interface CommentService {
    IPage<CommentVo> pageRootItem(IPage<CommentVo> page, CommentQueryVo queryVo);

    Long insertOne(Comment comment);

    IPage<CommentVo> pageReplyItem(IPage<CommentVo> page, CommentQueryVo queryVo);

    void logicDelete(CommentQueryVo queryVo);
}

package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Comment;
import com.video.web.main.vo.CommentQueryVo;
import com.video.web.main.vo.CommentVo;

public interface CommentMapper extends BaseMapper<Comment> {
    IPage<CommentVo> pageRootItem(IPage<CommentVo> page, CommentQueryVo queryVo);

    IPage<CommentVo> pageReplyItem(IPage<CommentVo> page, CommentQueryVo queryVo);
}

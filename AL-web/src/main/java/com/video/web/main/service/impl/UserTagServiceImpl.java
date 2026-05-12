package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.common.result.Result;
import com.video.model.entity.UserTag;
import com.video.web.main.mapper.UserTagMapper;
import com.video.web.main.service.UserTagService;
import com.video.web.main.vo.RecQueryVo;
import com.video.web.main.vo.TagRankListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag> implements UserTagService {
    @Autowired
    private UserTagMapper userTagMapper;

    @Override
    public Result<List<String>> getCommonTags(RecQueryVo queryVo) {
        List<String> res = userTagMapper.getCommonTags(queryVo.getUid(), queryVo.getVid());
        return Result.ok(res);
    }

    @Override
    public Result<List<TagRankListVO>> getTagRankList(Long vid) {
        List<TagRankListVO> tagRankListVOs = userTagMapper.getTagRankList(vid);
        return Result.ok(tagRankListVOs);
    }
}

package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.common.result.Result;
import com.video.model.entity.UserTag;
import com.video.model.enums.TagType;
import com.video.web.main.mapper.UserTagMapper;
import com.video.web.main.service.UserTagService;
import com.video.web.main.vo.RecQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.video.common.result.ResultCodeEnum.APP_SERVER_ERROR;
import static com.video.common.result.ResultCodeEnum.PARAM_ERROR;

@Service
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag> implements UserTagService {
    @Autowired
    private UserTagMapper userTagMapper;

    @Override
    public Result<List<String>> getCommonTags(RecQueryVo queryVo) {
        List<String> res = userTagMapper.getCommonTags(queryVo.getUid(), queryVo.getVid());
        return Result.ok(res);
    }
}

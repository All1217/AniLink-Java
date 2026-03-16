package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Follow;
import com.video.model.entity.UserInfo;
import com.video.web.main.mapper.FansMapper;
import com.video.web.main.service.FansService;
import com.video.web.main.vo.FollowQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FansServiceImpl implements FansService {
    @Autowired
    private FansMapper fansMapper;

    @Override
    public void insertOne(Follow follow) {
        follow.setId(0L);
        follow.setCreatedAt(new Date());
        fansMapper.insertOne(follow);
    }

    @Override
    public void deleteById(Follow follow) {
        fansMapper.deleteById(follow);
    }

    @Override
    public Follow getFollow(Follow follow) {
        return fansMapper.getFollow(follow);
    }

    @Override
    public IPage<UserInfo> getFollowList(IPage<UserInfo> page, FollowQueryVo query) {
        return fansMapper.getFollowList(page, query);
    }

    @Override
    public IPage<UserInfo> getFansList(IPage<UserInfo> page, FollowQueryVo query) {
        return fansMapper.getFansList(page, query);
    }
}

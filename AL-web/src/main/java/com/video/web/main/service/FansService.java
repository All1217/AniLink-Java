package com.video.web.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Follow;
import com.video.model.entity.UserInfo;
import com.video.web.main.vo.FollowQueryVo;

public interface FansService {
    void insertOne(Follow follow);

    void deleteById(Follow follow);

    Follow getFollow(Follow follow);

    IPage<UserInfo> getFollowList(IPage<UserInfo> page, FollowQueryVo query);

    IPage<UserInfo> getFansList(IPage<UserInfo> page, FollowQueryVo query);
}

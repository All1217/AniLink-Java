package com.video.web.main.service;

import com.video.common.result.Result;
import com.video.model.entity.UserInfo;
import com.video.model.entity.UserStats;
import com.video.web.main.vo.*;

import java.util.List;

public interface UserInfoService {
    UserInfo getById(Long id);

    String insertOne(RegisterVo registerVo);

    UserStats getUserStatsByUid(Long uid);

    void updateNotice(UserStatsQueryVo queryVo);

    void updateDescription(UserInfoQueryVo queryVo);

    void updateUserStats(Long uid);

    void updateUserInfo(UserInfoQueryVo queryVo);

    UserVo getUserVoByUid(UserInfoQueryVo queryVo);

    void updateAvatar(UserInfoQueryVo queryVo);

    Result<List<UserInfo>> getSimilarUsers(List<Long> uids);
}

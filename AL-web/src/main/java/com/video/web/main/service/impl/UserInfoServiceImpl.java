package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.common.exception.VideoException;
import com.video.common.result.Result;
import com.video.common.utils.CodeUtil;
import com.video.model.entity.Favorite;
import com.video.model.entity.UserInfo;
import com.video.model.entity.UserStats;
import com.video.web.main.mapper.UserInfoMapper;
import com.video.web.main.service.FavoriteService;
import com.video.web.main.service.UserInfoService;
import com.video.web.main.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.video.common.constant.Constant.UNIQUE_UID_KEY;
import static com.video.common.constant.MinioConstant.DEFAULT_AVATAR;
import static com.video.common.result.ResultCodeEnum.APP_SERVER_ERROR;

@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoMapper uiMapper;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private final BoundValueOperations<String, String> serialOps;

    public UserInfoServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.serialOps = redisTemplate.boundValueOps(UNIQUE_UID_KEY);
    }

    @Override
    public UserInfo getById(Long id) {
        return uiMapper.selectOneByUsername(id.toString());
    }

    /**
     * 注册业务逻辑
     */
    @Override
    @Transactional
    public String insertOne(RegisterVo registerVo) {
        //生成UID
        Long sequence = serialOps.increment(1);
        if (sequence == null) {
            throw new VideoException(APP_SERVER_ERROR.getCode(), "服务器异常：生成自增序列号失败！");
        }
        Long uid = CodeUtil.geneVisibleCode(sequence, sequence);
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(uid);
        userInfo.setUsername("" + uid);
        userInfo.setAvatar(DEFAULT_AVATAR);
        userInfo.setNickname(registerVo.getNickname());
        userInfo.setPassword(DigestUtils.md5DigestAsHex(registerVo.getPassword().getBytes()));
        userInfo.setCreateDate(new Date());
        uiMapper.insert(userInfo);
        //初始化用户额外数据
        UserStats userStats = new UserStats();
        userStats.setUid(uid);
        uiMapper.inserUserStats(userStats);
        //初始化收藏夹
        Favorite tf = new Favorite();
        tf.setType(1);
        tf.setTitle("默认收藏夹");
        tf.setDescription("默认收藏夹");
        tf.setUid(uid);
        tf.setVisible(1);
        favoriteService.insertOne(tf);
        return "" + uid;
    }

    @Override
    public UserStats getUserStatsByUid(Long uid) {
        return uiMapper.getUserStatsByUid(uid);
    }

    @Override
    public void updateNotice(UserStatsQueryVo queryVo) {
        UserStats userStats = new UserStats();
        userStats.setUid(queryVo.getUid());
        userStats.setNotice(queryVo.getNotice());
        uiMapper.updateUserStats(userStats);
    }

    @Override
    public void updateDescription(UserInfoQueryVo queryVo) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(queryVo.getUid());
        userInfo.setDescription(queryVo.getDescription());
        uiMapper.updateUserInfo(userInfo);
    }

    @Override
    @Transactional
    public void updateUserStats(Long uid) {
        long fansCount = uiMapper.calFansCount(uid);
        int followCount = uiMapper.calFollowCount(uid);
        long likeCount = uiMapper.calLikeCount(uid);
        long playCount = uiMapper.calPlayCount(uid);
        int videoCount = uiMapper.calVideoCount(uid);
        //TODO
//        int collectionCount = uiMapper.calCollectionCount(uid);
        int collectCount = uiMapper.calCollectCount(uid);
        UserStats userStats = new UserStats();
        userStats.setUid(uid);
        userStats.setFansCount(fansCount);
        userStats.setFollowCount(followCount);
        userStats.setLikeCount(likeCount);
        userStats.setPlayCount(playCount);
        userStats.setVideoCount(videoCount);
        userStats.setCollectCount(collectCount);
        uiMapper.updateUserStats(userStats);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserInfoQueryVo queryVo) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(queryVo.getUid());
        userInfo.setAvatar(queryVo.getAvatar());
        userInfo.setNickname(queryVo.getNickname());
        userInfo.setGender(queryVo.getGender());
        userInfo.setDescription(queryVo.getDescription());
        uiMapper.updateUserInfo(userInfo);

        UserStats userStats = new UserStats();
        userStats.setUid(queryVo.getUid());
        userStats.setSchool(queryVo.getSchool());
        userStats.setBirthDate(queryVo.getBirthDate());
        log.info("impl userStats: {}", userStats);
        uiMapper.updateUserStats(userStats);
    }

    @Override
    public UserVo getUserVoByUid(UserInfoQueryVo queryVo) {
        return uiMapper.getUserVoByUid(queryVo);
    }

    @Override
    public void updateAvatar(UserInfoQueryVo queryVo) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(queryVo.getUid());
        userInfo.setAvatar(queryVo.getAvatar());
        uiMapper.updateUserInfo(userInfo);
    }

    @Override
    public Result<List<UserInfo>> getSimilarUsers(List<Long> uids) {
        List<UserInfo> res = lambdaQuery()
                .in(UserInfo::getUid, uids)
                .list();
        Collections.reverse(res);
        return Result.ok(res);
    }
}

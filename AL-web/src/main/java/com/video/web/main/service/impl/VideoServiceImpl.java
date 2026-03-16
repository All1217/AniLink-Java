package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.common.constant.Constant;
import com.video.model.entity.UserVideo;
import com.video.model.entity.Video;
import com.video.model.entity.VideoStats;
import com.video.model.enums.ActionType;
import com.video.web.main.mapper.UserVideoMapper;
import com.video.web.main.mapper.VideoMapper;
import com.video.web.main.mapper.VideoStatsMapper;
import com.video.web.main.service.VideoService;
import com.video.web.main.vo.UserVideoQueryVo;
import com.video.web.main.vo.VideoQueryVo;
import com.video.web.main.vo.VideoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VideoStatsMapper videoStatsMapper;
    @Autowired
    private UserVideoMapper userVideoMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public void insertOne(Video video) {
        video.setUploadDate(new Date());
        videoMapper.insertOne(video);
        VideoStats videoStats = new VideoStats();
        videoStats.setVid(video.getVid());
        videoStats.setPlay(0L);
        videoStats.setDanmu(0L);
        videoStats.setGood(0L);
        videoStats.setBad(0L);
        videoStats.setCoin(0L);
        videoStats.setCollect(0L);
        videoStats.setShare(0L);
        videoStats.setComment(0L);
        videoStatsMapper.insertOneStats(videoStats);
    }

    @Override
    public IPage<VideoVo> pageItem(IPage<VideoVo> page, VideoQueryVo queryVo) {
        return videoMapper.pageItem(page, queryVo);
    }

    @Override
    public Video getById(Long vid) {
        return videoMapper.getById(vid);
    }

    @Override
    public VideoStats getVideoStatsById(Long vid) {
        return videoStatsMapper.getVideoStatsById(vid);
    }

    @Override
    @Transactional
    public void recommend(UserVideoQueryVo userVideoQueryVo) {
        boolean flag = true;
        UserVideo userVideo = userVideoMapper.selectActionById(userVideoQueryVo.getUid(), userVideoQueryVo.getVid());
        if (userVideo == null) {
            userVideo = new UserVideo();
            userVideo.setVid(userVideoQueryVo.getVid());
            userVideo.setUid(userVideoQueryVo.getUid());
            userVideo.setCoin(0);
            userVideo.setLove(0);
            userVideo.setUnlove(0);
            userVideo.setCollect(0);
            userVideo.setPlay(0);
            userVideo.setPlayTime(new Date());
            flag = false;
        }
        if (Objects.equals(userVideoQueryVo.getActionType(), ActionType.LIKE.getNum())) {
            userVideo.setUnlove(0);
            if (userVideo.getLove() >= 1) {
                userVideo.setLove(0);
            } else {
                userVideo.setLove(1);
            }
            userVideo.setLoveTime(userVideoQueryVo.getActionTime());
        } else if (Objects.equals(userVideoQueryVo.getActionType(), ActionType.DISLIKE.getNum())) {
            userVideo.setLove(0);
            if (userVideo.getUnlove() >= 1) {
                userVideo.setUnlove(0);
            } else {
                userVideo.setUnlove(1);
            }
            userVideo.setLoveTime(userVideoQueryVo.getActionTime());
        } else if (Objects.equals(userVideoQueryVo.getActionType(), ActionType.COIN_ONE.getNum())) {
            if (userVideo.getCoin() >= 1) {
                userVideo.setCoin(0);
            } else {
                userVideo.setCoin(1);
            }
            userVideo.setCoinTime(userVideoQueryVo.getActionTime());
        } else if (Objects.equals(userVideoQueryVo.getActionType(), ActionType.COIN_TWO.getNum())) {
            if (userVideo.getCoin() >= 1) {
                userVideo.setCoin(0);
            } else {
                userVideo.setCoin(2);
            }
            userVideo.setCoinTime(userVideoQueryVo.getActionTime());
        } else if (Objects.equals(userVideoQueryVo.getActionType(), ActionType.PLAY.getNum())) {
            userVideo.setPlay(userVideo.getPlay() + 1);
            userVideo.setPlayTime(userVideoQueryVo.getActionTime());
        }
        //原先不走缓存，直接入库
        if (flag) {
            userVideoMapper.updateAction(userVideo);
        } else {
            userVideoMapper.insertAction(userVideo);
        }
        updateVideoStats(userVideoQueryVo.getVid());
    }

    @Override
    public UserVideo getInterActionStats(UserVideoQueryVo userVideoQueryVo) {
        return userVideoMapper.getInterActionStats(userVideoQueryVo.getUid(), userVideoQueryVo.getVid());
    }

    @Transactional
    public void updateVideoStats(Long vid) {
        long play = videoStatsMapper.calPlay(vid);
        long collect = videoStatsMapper.calCollect(vid);
        long danmu = videoStatsMapper.calDanmu(vid);
        long like = videoStatsMapper.calLike(vid);
        long comment = videoStatsMapper.calComment(vid);
        long coin = videoStatsMapper.calCoin(vid);
        VideoStats videoStats = new VideoStats();
        videoStats.setVid(vid);
        videoStats.setPlay(play);
        videoStats.setComment(comment);
        videoStats.setCollect(collect);
        videoStats.setCoin(coin);
        videoStats.setDanmu(danmu);
        videoStats.setGood(like);
        log.info("更新视频数据：{}", videoStats);
        videoStatsMapper.updateVideoStats(videoStats);
    }

    @Override
    @Transactional
    public List<VideoVo> getRealTimeRecommend(List<Long> videoIds) {
        return videoMapper.batchSelect(videoIds);
    }

    @Override
    public UserVideo interActLike(UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = new UserVideo();
        String videoKey = Constant.MAIN_VIDEO_LIKE_PREFIX + userVideoQueryVo.getVid();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(videoKey))) {
            Integer tCount = (Integer) redisTemplate.opsForHash().get(videoKey, userVideoQueryVo.getUid());
            if (tCount == null) {
                redisTemplate.opsForHash().put(videoKey, userVideoQueryVo.getUid(), 1);
                res.setLove(1);
            } else {
                if (tCount == 0) {
                    redisTemplate.opsForHash().put(videoKey, userVideoQueryVo.getUid(), 1);
                    res.setLove(1);
                } else {
                    redisTemplate.opsForHash().put(videoKey, userVideoQueryVo.getUid(), 0);
                    res.setLove(0);
                }
            }
        } else {
            redisTemplate.opsForHash().put(videoKey, userVideoQueryVo.getUid(), 1);
            res.setLove(1);
        }
        return res;
    }

    @Override
    @Transactional
    public UserVideo getInterActLike(UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = new UserVideo();
        String videoKey = Constant.MAIN_VIDEO_LIKE_PREFIX + userVideoQueryVo.getVid();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(videoKey))) {
            Integer tCount = (Integer) redisTemplate.opsForHash().get(videoKey, userVideoQueryVo.getUid());
            if (tCount != null) res.setLove(tCount);
            else {
                UserVideo doc = videoStatsMapper.getInterActLike(userVideoQueryVo);
                res.setLove(doc == null ? 0 : doc.getLove());
            }
        } else {
            UserVideo doc = videoStatsMapper.getInterActLike(userVideoQueryVo);
            res.setLove(doc == null ? 0 : doc.getLove());
        }
        return res;
    }

    @Override
    @Transactional
    public UserVideo getInterActionCoin(UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = new UserVideo();
        String videoKey = Constant.MAIN_VIDEO_COIN_PREFIX + userVideoQueryVo.getVid();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(videoKey))) {
            Integer tCount = (Integer) redisTemplate.opsForHash().get(videoKey, userVideoQueryVo.getUid());
            if (tCount != null) res.setCoin(tCount);
            else {
                UserVideo temp = videoStatsMapper.getInterActionCoin(userVideoQueryVo);
                res.setCoin(temp == null ? 0 : temp.getCoin());
            }
        } else {
            UserVideo temp = videoStatsMapper.getInterActionCoin(userVideoQueryVo);
            res.setCoin(temp == null ? 0 : temp.getCoin());
        }
        return res;
    }

    @Override
    public UserVideo interActCoin(UserVideoQueryVo userVideoQueryVo) {
        Integer cnt = userVideoQueryVo.getActionType();
        UserVideo res = new UserVideo();
        String videoKey = Constant.MAIN_VIDEO_COIN_PREFIX + userVideoQueryVo.getVid();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(videoKey))) {
            Integer tCount = (Integer) redisTemplate.opsForHash().get(videoKey, userVideoQueryVo.getUid());
            if (tCount != null) res.setCoin(tCount);
            else {
                redisTemplate.opsForHash().put(videoKey, userVideoQueryVo.getUid(), cnt);
                res.setCoin(cnt);
            }
        } else {
            redisTemplate.opsForHash().put(videoKey, userVideoQueryVo.getUid(), cnt);
            res.setCoin(cnt);
        }
        return res;
    }

    @Override
    public void playVideo(UserVideoQueryVo userVideoQueryVo) {
        String videoKey = Constant.MAIN_VIDEO_PLAY_PREFIX + userVideoQueryVo.getVid();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(videoKey))) {//缓存存在videoKey对应的哈希表
            Integer tCount = (Integer) redisTemplate.opsForHash().get(videoKey, userVideoQueryVo.getUid());
            if (tCount == null) redisTemplate.opsForHash().put(videoKey, userVideoQueryVo.getUid(), 1);
            else redisTemplate.opsForHash().put(videoKey, userVideoQueryVo.getUid(), 1 + tCount);
        } else {//缓存没有videoKey对应的哈希表
            //直接将对应播放记录置1
            redisTemplate.opsForHash().put(videoKey, userVideoQueryVo.getUid(), 1);
        }
    }
}

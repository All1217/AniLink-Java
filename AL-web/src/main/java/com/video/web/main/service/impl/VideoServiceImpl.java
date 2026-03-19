package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.common.constant.Constant;
import com.video.model.entity.UserVideo;
import com.video.model.entity.Video;
import com.video.model.entity.VideoStats;
import com.video.web.main.mapper.VideoMapper;
import com.video.web.main.mapper.VideoStatsMapper;
import com.video.web.main.service.VideoService;
import com.video.web.main.vo.UserVideoQueryVo;
import com.video.web.main.vo.VideoQueryVo;
import com.video.web.main.vo.VideoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoMapper videoMapper;
    private final VideoStatsMapper videoStatsMapper;
    private final RedisTemplate<String, Object> redisTemplate;

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
    public List<VideoVo> getRealTimeRecommend(List<Long> videoIds) {
        return videoMapper.batchSelect(videoIds);
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
}

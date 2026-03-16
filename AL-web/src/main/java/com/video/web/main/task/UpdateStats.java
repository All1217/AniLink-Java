package com.video.web.main.task;

import com.video.common.constant.Constant;
import com.video.common.exception.VideoException;
import com.video.common.result.ResultCodeEnum;
import com.video.model.entity.UserVideo;
import com.video.model.entity.VideoStats;
import com.video.web.main.mapper.UserVideoMapper;
import com.video.web.main.mapper.VideoStatsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Slf4j
public class UpdateStats {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private VideoStatsMapper videoStatsMapper;
    @Autowired
    private UserVideoMapper userVideoMapper;

    @Scheduled(cron = "0/30 * * * * ?")
    @Transactional
    public void calVideoStats() {
        log.info("开始计算交互数据……");
        Map<String, UserVideo> userVideoMap = new HashMap<>();
        Map<String, VideoStats> videoStatsMap = new HashMap<>();
        processVideoStats(Constant.MAIN_VIDEO_COIN_PREFIX, "coin", userVideoMap, videoStatsMap);
        processVideoStats(Constant.MAIN_VIDEO_FAVORITE_PREFIX, "collect", userVideoMap, videoStatsMap);
        processVideoStats(Constant.MAIN_VIDEO_LIKE_PREFIX, "good", userVideoMap, videoStatsMap);
        processVideoStats(Constant.MAIN_VIDEO_PLAY_PREFIX, "play", userVideoMap, videoStatsMap);
        List<VideoStats> videoStatsList = new ArrayList<>(videoStatsMap.values());
        List<UserVideo> userVideoList = new ArrayList<>(userVideoMap.values());
        //更新用户-视频关系表
        //一次性优化语句老出问题
//        if(!videoStatsList.isEmpty()) videoStatsMapper.batchAddStats(videoStatsList);
        if (!userVideoList.isEmpty()) userVideoMapper.batchUpdateStats(userVideoList);

        //更新视频数据表
        videoStatsList.forEach(vs -> {
            videoStatsMapper.addVideoStats(vs);
        });
    }

    private void processVideoStats(String redisPrefix, String fieldName,
                                   Map<String, UserVideo> userVideoMap, Map<String, VideoStats> videoStatsMap) {
        Set<String> videoKeys = redisTemplate.keys(redisPrefix + "*");
        if (videoKeys != null) {
            for (String videoKey : videoKeys) {
                long videoId = Long.parseLong(videoKey.split(":")[3]);
                Map<Object, Object> data = redisTemplate.opsForHash().entries(videoKey);
                long total = 0;
                // 遍历每个交互数据
                for (Map.Entry<Object, Object> entry : data.entrySet()) {//遍历每个用户对应的数据
                    Long userId = Long.valueOf(entry.getKey().toString());
                    Integer value = (Integer) entry.getValue();
                    if (value >= 1) {
                        total += value;
                        // 更新 userVideoMapmap
                        String mapKey = userId + ":" + videoId;
                        userVideoMap.putIfAbsent(mapKey, getDefaultUserVideo());
                        userVideoMap.get(mapKey).setUid(userId);
                        userVideoMap.get(mapKey).setVid(videoId);
                        // 根据不同的 fieldName 设置相应的值
                        switch (fieldName) {
                            case "coin":
                                userVideoMap.get(mapKey).setCoin(value);
                                userVideoMap.get(mapKey).setCoinTime(new Date());
                                break;
                            case "collect":
                                userVideoMap.get(mapKey).setCollect(value);
                                userVideoMap.get(mapKey).setCollectTime(new Date());
                                break;
                            case "good":
                                userVideoMap.get(mapKey).setLove(value);
                                userVideoMap.get(mapKey).setLoveTime(new Date());
                                break;
                            case "play":
                                userVideoMap.get(mapKey).setPlay(value);
                                userVideoMap.get(mapKey).setPlayTime(new Date());
                                break;
                            default:
                                throw new VideoException(ResultCodeEnum.TASK_UNKNOWN_FIELD);
                        }
                    }
                }
                // 更新 videoStatsMap
                VideoStats t = new VideoStats();
                t.setVid(videoId);
                videoStatsMap.putIfAbsent("" + videoId, t);
                switch (fieldName) {
                    case "coin":
                        videoStatsMap.get("" + videoId).setCoin(total);
                        break;
                    case "collect":
                        videoStatsMap.get("" + videoId).setCollect(total);
                        break;
                    case "good":
                        videoStatsMap.get("" + videoId).setGood(total);
                        break;
                    case "play":
                        videoStatsMap.get("" + videoId).setPlay(total);
                        break;
                    default:
                        throw new VideoException(ResultCodeEnum.TASK_UNKNOWN_FIELD);
                }
                if (Boolean.TRUE.equals(redisTemplate.hasKey(videoKey))) {
                    redisTemplate.delete(videoKey);
                }
            }
        }
    }

    public UserVideo getDefaultUserVideo() {
        UserVideo t = new UserVideo();
        t.setCoin(0);
        t.setLove(0);
        t.setUnlove(0);
        t.setPlay(0);
        t.setCollect(0);
        t.setPlayTime(new Date());
        return t;
    }
}

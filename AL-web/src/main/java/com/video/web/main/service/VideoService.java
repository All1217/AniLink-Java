package com.video.web.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.UserVideo;
import com.video.model.entity.Video;
import com.video.model.entity.VideoStats;
import com.video.web.main.vo.UserVideoQueryVo;
import com.video.web.main.vo.VideoQueryVo;
import com.video.web.main.vo.VideoVo;

import java.util.List;

public interface VideoService {
    void insertOne(Video video);

    IPage<VideoVo> pageItem(IPage<VideoVo> page, VideoQueryVo queryVo);

    Video getById(Long vid);

    VideoStats getVideoStatsById(Long vid);

    void recommend(UserVideoQueryVo userVideoQueryVo);

    UserVideo getInterActionStats(UserVideoQueryVo userVideoQueryVo);

    List<VideoVo> getRealTimeRecommend(List<Long> videoIds);

    UserVideo interActLike(UserVideoQueryVo userVideoQueryVo);

    UserVideo getInterActLike(UserVideoQueryVo userVideoQueryVo);

    UserVideo getInterActionCoin(UserVideoQueryVo userVideoQueryVo);

    UserVideo interActCoin(UserVideoQueryVo userVideoQueryVo);

    void playVideo(UserVideoQueryVo userVideoQueryVo);
}

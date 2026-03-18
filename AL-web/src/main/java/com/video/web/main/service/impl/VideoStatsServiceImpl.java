package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.model.entity.VideoStats;
import com.video.web.main.mapper.VideoStatsMapper;
import com.video.web.main.service.VideoStatsService;
import org.springframework.stereotype.Service;

@Service
public class VideoStatsServiceImpl extends ServiceImpl<VideoStatsMapper, VideoStats> implements VideoStatsService {
}

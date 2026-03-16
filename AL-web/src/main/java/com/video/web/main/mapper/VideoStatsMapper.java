package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.model.entity.UserVideo;
import com.video.model.entity.VideoStats;
import com.video.web.main.vo.UserVideoQueryVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VideoStatsMapper extends BaseMapper<VideoStats> {
    @Select("select * from user_video where uid=#{uid} and vid=#{vid}")
    UserVideo getInterActLike(UserVideoQueryVo userVideoQueryVo);

    @Select("select * from user_video where uid=#{uid} and vid=#{vid}")
    UserVideo getInterActionCoin(UserVideoQueryVo userVideoQueryVo);

    @Insert("insert into video_stats (vid, play, danmu, good, bad, coin, collect, share, comment) " +
            "VALUES (#{vid}, #{play}, #{danmu}, #{good}, #{bad}, #{coin}, #{collect}, #{share}, #{comment})")
    void insertOneStats(VideoStats videoStats);

    @Select("select * from video_stats where vid = #{vid}")
    VideoStats getVideoStatsById(Long vid);

    void updateVideoStats(VideoStats videoStats);

    void addVideoStats(VideoStats videoStats);

    @Select("select SUM(play) from user_video where vid = #{vid}")
    long calPlay(Long vid);

    @Select("select COUNT(*) from danmu where vid = #{vid} and status = 1")
    long calDanmu(Long vid);

    @Select("select SUM(love) from user_video where vid = #{vid}")
    long calLike(Long vid);

    @Select("select SUM(coin) from user_video where vid = #{vid}")
    long calCoin(Long vid);

    @Select("select SUM(collect) from user_video where vid = #{vid}")
    long calCollect(Long vid);

    @Select("select COUNT(*) from comment where vid = #{vid} and root_id = 0 and is_deleted = 0")
    long calComment(Long vid);

    void batchAddStats(List<VideoStats> list);
}

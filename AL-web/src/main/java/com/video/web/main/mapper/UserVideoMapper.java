package com.video.web.main.mapper;

import com.video.model.entity.UserVideo;
import com.video.model.entity.VideoStats;
import com.video.web.main.vo.UserVideoQueryVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserVideoMapper {
    @Select("select * from user_video where uid = #{uid} and vid = #{vid}")
    UserVideo selectActionById(Long uid, Long vid);

    @Insert("insert into user_video (uid, vid, play, love, unlove, coin, collect, play_time, love_time, coin_time) " +
            "values (#{uid}, #{vid}, #{play}, #{love}, #{unlove}, #{coin}, #{collect}, #{playTime}, #{loveTime}, #{coinTime})")
    void insertAction(UserVideo userVideo);

    void updateAction(UserVideo userVideo);

    VideoStats getStatsBySum(UserVideoQueryVo userVideoQueryVo);

    @Select("select * from user_video where vid = #{vid} and uid = #{uid}")
    UserVideo getInterActionStats(Long uid, Long vid);

    void batchUpdateStats(List<UserVideo> list);
}

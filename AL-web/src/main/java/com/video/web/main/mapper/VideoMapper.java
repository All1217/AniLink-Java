package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.UserVideo;
import com.video.model.entity.Video;
import com.video.model.entity.VideoStats;
import com.video.web.main.vo.UserVideoQueryVo;
import com.video.web.main.vo.VideoQueryVo;
import com.video.web.main.vo.VideoVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VideoMapper extends BaseMapper<Video> {
    @Insert("insert into video (uid, title, type, auth, duration, mc_id, sc_id, tags, descr, status, visible, cover_url, video_url, upload_date, delete_date) values (#{uid}, #{title}, #{type}, #{auth}, #{duration}, #{mcId}, #{scId}, #{tags}, #{descr}, #{status}, #{visible}, #{coverUrl}, #{videoUrl}, #{uploadDate}, #{deleteDate})")
    @Options(useGeneratedKeys = true, keyProperty = "vid", keyColumn = "vid")
    void insertOne(Video video);

    IPage<VideoVo> pageItem(IPage<VideoVo> page, VideoQueryVo queryVo);

    @Select("SELECT * from video where vid = #{vid} and visible = 0 and status = 1")
    Video getById(Long vid);

    List<VideoVo> batchSelect(List<Long> videoIds);
}

package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.model.entity.Danmu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DanmuMapper extends BaseMapper<Danmu> {
    List<Danmu> filterUserByTags(Long vid, String tagName);

    @Delete("delete from sub_danmu where status = 1;")
    void deleteOriginSubDanmu();

    @Insert("insert into sub_danmu values " +
            "(#{d.id}, #{d.vid}, #{d.uid}, #{d.content}, #{d.fontsize}, #{d.mode}, #{d.color}, #{d.timePoint}, 1, #{d.createDate});")
    void insertSubDanmu(@Param("d") Danmu d);
}

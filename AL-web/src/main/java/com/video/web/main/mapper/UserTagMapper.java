package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.model.entity.UserTag;
import com.video.web.main.vo.TagRankListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserTagMapper extends BaseMapper<UserTag> {
    List<String> getCommonTags(Long uid, Long vid);

    List<TagRankListVO> getTagRankList(@Param("vid") Long vid);
}

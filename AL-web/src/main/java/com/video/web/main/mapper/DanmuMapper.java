package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.model.entity.Danmu;

import java.util.List;

public interface DanmuMapper extends BaseMapper<Danmu> {
    List<Danmu> filterUserByTags(Long vid, String tagName);
}

package com.video.web.main.mapper;

import java.util.Map;

public interface DataCenterMapper {
    Long sumPlayByMap(Map<String, Object> map);

    Long sumFansPlayByMap(Map<String, Object> map);

    Long sumLikeByMap(Map<String, Object> map);
}

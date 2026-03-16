package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户额外信息查询参数")
public class UserStatsQueryVo {
    @Schema(description="用户名")
    private Long uid;

    @Schema(description="公告")
    private String notice;
}

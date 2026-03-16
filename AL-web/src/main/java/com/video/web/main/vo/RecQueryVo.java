package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "推荐服务Vo")
public class RecQueryVo {
    @Schema(description="用户ID")
    private Long uid;

    @Schema(description="推荐视频数量")
    private Integer count;

    @Schema(description="视频ID/用户ID")
    private Long vid;

    @Schema(description="指定的用户标签")
    private Integer tag;
}

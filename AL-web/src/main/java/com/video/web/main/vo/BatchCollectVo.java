package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "将一个视频添加到0个或多个收藏")
public class BatchCollectVo {
    @Schema(description = "视频ID")
    private Long vid;
    @Schema(description = "收藏夹ID")
    private List<Long> fids;
    @Schema(description = "用户ID")
    private Long uid;
}

package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
@Schema(description = "数据中心总览")
public class DataTotal {
    @Schema(description="播放量")
    private CellItem play;
    @Schema(description="点赞")
    private CellItem like;
    @Schema(description="收藏")
    private CellItem collect;
    @Schema(description="硬币")
    private CellItem coin;
}

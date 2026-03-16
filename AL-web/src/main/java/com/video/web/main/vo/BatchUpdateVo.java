package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "修改视频所在收藏夹")
public class BatchUpdateVo {
    @Schema(description = "视频ID")
    private List<Long> vids;
    @Schema(description = "旧收藏夹")
    private Long preFid;
    @Schema(description = "新收藏夹")
    private Long newFid;
}

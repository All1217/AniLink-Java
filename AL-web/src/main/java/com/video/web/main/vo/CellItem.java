package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "数据中心总览")
public class CellItem {
    @Schema(description="总量")
    private Long total;
    @Schema(description="今日数据")
    private Long today;
    @Schema(description="昨日数据")
    private Long yesterday;
}

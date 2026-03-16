package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "数据分析结果Vo")
public class DataResVo {
    @Schema(description="结果集1")
    private List<Long> mainRes;
    @Schema(description="结果集2")
    private List<Long> subRes;
    @Schema(description="上周期结果集1")
    private List<Long> lastMainRes;
    @Schema(description="上周期结果集2")
    private List<Long> lastSubRes;
}

package com.video.web.main.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "历史记录查询条件")
public class HistoryQueryVo {
    @Schema(description = "用户UID")
    private Long uid;
    @Schema(description = "关键词")
    private String keyword;
    @Schema(description = "时长下限")
    private Integer startDuration;
    @Schema(description = "时长上限")
    private Integer endDuration;
    @Schema(description="开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @Schema(description="结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @Schema(description = "VID列表")
    private List<Long> vids;
}

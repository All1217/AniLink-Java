package com.video.web.main.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.Date;

@Data
@Schema(description = "视频查询条件")
public class VideoQueryVo{
    @Schema(description="作者ID")
    private Long uid;

    @Schema(description="标题")
    private String title;

    @Schema(description="排序方式")
    private Integer sortType;

    @Schema(description="开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @Schema(description="结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @Schema(description="时长范围")
    private Double startDuration;

    @Schema(description="时长范围")
    private Double endDuration;

    @Schema(description="分区ID")
    private Long mcId;

    @Schema(description="排序规则")
    private String sortField;
}

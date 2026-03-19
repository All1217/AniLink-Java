package com.video.model.dto.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "视频播放记录表单")
public class VideoRecordFormDTO {
    @Schema(description = "视频ID")
    private Long vid;

    @Schema(description = "视频总时长，单位秒")
    private Integer duration;

    @Schema(description = "视频的当前观看时长，单位秒")
    private Double moment;

    @Schema(description = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime commitTime;
}

package com.video.web.main.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.video.model.entity.VideoStats;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "视频信息（Vo）")
public class VideoVo extends VideoStats {
    @Schema(description="作者ID")
    private Long uid;

    @Schema(description="作者昵称")
    private String nickname;

    @Schema(description="标题")
    private String title;

    @Schema(description="时长")
    private Double duration;

    @Schema(description="封面")
    private String coverUrl;

    @Schema(description="上传时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadDate;
}

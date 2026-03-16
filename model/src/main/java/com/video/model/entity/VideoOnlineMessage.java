package com.video.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "在线观看视频消息实体")
@Data
public class VideoOnlineMessage {
    @Schema(description = "uid")
    private String uid;
    @Schema(description = "id")
    private String id;
    @Schema(description = "vid")
    private String vid;
    @Schema(description = "是否是弹幕")
    private boolean type;
    @Schema(description = "弹幕内容")
    private String message;
    @Schema(description = "增减人数")
    private Integer cnt;
}

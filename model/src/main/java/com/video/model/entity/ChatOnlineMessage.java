package com.video.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "在线聊天所用的消息实体类")
@Data
public class ChatOnlineMessage {
    @Schema(description = "来源uid")
    private String from;
    @Schema(description = "目标uid")
    private String to;
    @Schema(description = "是否是系统消息")
    private boolean isSystem;
    @Schema(description = "消息内容")
    private String message;
}

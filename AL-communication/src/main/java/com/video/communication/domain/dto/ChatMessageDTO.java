package com.video.communication.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "在线聊天所用的消息实体类")
@Data
public class ChatMessageDTO {
    @Schema(description = "来源uid")
    private String from;
    @Schema(description = "目标uid")
    private String to;
    @Schema(description = "消息类型")
    private String type;
    @Schema(description = "消息内容")
    private String content;
    @Schema(description = "jwt token")
    private String token;
}

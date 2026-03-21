package com.video.communication.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "客户端-->视频在线人数统计，DTO")
@Data
public class CountMessageDTO {
    @Schema(description = "视频id")
    private String vid;
    @Schema(description = "jwt token")
    private String token;
    @Schema(description = "消息类型")
    private String type;
}

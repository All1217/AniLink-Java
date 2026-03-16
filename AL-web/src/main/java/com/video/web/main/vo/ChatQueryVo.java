package com.video.web.main.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "聊天对象/消息查询条件")
public class ChatQueryVo {
    @Schema(description = "ID")
    private Long id;
    @Schema(description = "最近打开时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date time;
    @Schema(description = "发起者")
    private Long senderId;
    @Schema(description = "接收者")
    private Long targetId;
    @Schema(description = "补充uid")
    private Long uid;
}

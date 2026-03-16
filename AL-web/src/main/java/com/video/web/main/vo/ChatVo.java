package com.video.web.main.vo;

import com.video.model.entity.Chat;
import com.video.model.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "聊天对象(Vo)")
public class ChatVo extends Chat {
    @Schema(description="发起者昵称")
    private String senderName;
    @Schema(description="接受者昵称")
    private String targetName;
    @Schema(description="对象头像")
    private String senderAvatar;
    @Schema(description="对象头像")
    private String targetAvatar;
    @Schema(description="最近一条消息")
    private ChatMessage chatMessage;
    @Schema(description="未读消息数")
    private Integer unreadCount;
}

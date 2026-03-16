package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema(description = "聊天消息表")
@TableName(value = "chat_message")
@Data
public class ChatMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "ID")
    @TableField(value = "id")
    @TableId(type = IdType.AUTO)
    private Long id;
    @Schema(description = "发送者ID")
    @TableField(value = "sender_id")
    private Long senderId;
    @Schema(description = "接受者ID")
    @TableField(value = "target_id")
    private Long targetId;
    @Schema(description = "内容")
    @TableField(value = "content")
    private String content;
    @Schema(description = "发送者是否删除")
    @TableField(value = "sender_del")
    private Integer senderDel;
    @Schema(description = "接收者是否删除")
    @TableField(value = "target_del")
    private Integer targetDel;
    @Schema(description = "是否撤回")
    @TableField(value = "withdraw")
    private Integer withdraw;
    @Schema(description = "消息类型")
    @TableField(value = "message_type")
    private Integer messageType;
    @Schema(description = "是否已读")
    @TableField(value = "read_status")
    private Integer readStatus;
    @Schema(description = "发送时间")
    @TableField(value = "time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
}

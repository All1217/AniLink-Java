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

@Schema(description = "聊天对象")
@TableName(value = "chat")
@Data
public class Chat implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "唯一标识")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "对象ID")
    @TableField(value = "target_id")
    private Long targetId;

    @Schema(description = "发起者ID")
    @TableField(value = "sender_id")
    private Long senderId;

    @Schema(description = "上一条消息的时间")
    @TableField(value = "latest_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date latestTime;
}

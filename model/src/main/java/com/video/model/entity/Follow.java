package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema(description = "关注与被关注表")
@TableName(value = "follow")
@Data
public class Follow implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableField(value = "id")
    private Long id;
    //关注者
    @TableField(value = "follower_id")
    private Long followerId;
    //被关注者
    @TableField(value = "followee_id")
    private Long followeeId;
    @Schema(description = "发送时间")
    @TableField(value = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
}

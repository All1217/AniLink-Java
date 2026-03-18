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

@Schema(description = "评论")
@TableName(value = "comment")
@Data
public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "视频ID")
    @TableField(value = "vid")
    private Long vid;

    @Schema(description = "发送者ID")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "根评论ID")
    @TableField(value = "root_id")
    private Long rootId;

    @Schema(description = "被回复的评论ID")
    @TableField(value = "parent_id")
    private Long parentId;

    @Schema(description = "被回复用户ID")
    @TableField(value = "to_user_id")
    private Long toUserId;

    @Schema(description = "评论内容")
    @TableField(value = "content")
    private String content;

    @Schema(description = "点赞数")
    @TableField(value = "love")
    private Integer love;

    @Schema(description = "点踩数")
    @TableField(value = "bad")
    private Integer bad;

    @Schema(description = "是否置顶")
    @TableField(value = "is_top")
    private Integer isTop;

    @Schema(description = "是否删除")
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @Schema(description = "评论时间")
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;
}

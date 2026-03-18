package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "视频数据表")
@TableName(value = "video_stats")
@Data
public class VideoStats implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "对应的视频ID")
    @TableId(type = IdType.AUTO)
    private Long vid;

    @Schema(description = "播放数")
    @TableField(value = "play")
    private Long play;

    @Schema(description = "弹幕数")
    @TableField(value = "danmu")
    private Long danmu;

    @Schema(description = "点赞数")
    @TableField(value = "good")
    private Long good;

    @Schema(description = "点踩数")
    @TableField(value = "bad")
    private Long bad;

    @Schema(description = "硬币数")
    @TableField(value = "coin")
    private Long coin;

    @Schema(description = "收藏数")
    @TableField(value = "collect")
    private Long collect;

    @Schema(description = "分享数")
    @TableField(value = "share")
    private Long share;

    @Schema(description = "评论数")
    @TableField(value = "comment")
    private Long comment;
}

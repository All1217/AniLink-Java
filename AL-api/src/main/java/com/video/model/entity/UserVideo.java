package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema(description = "用户与视频交互表")
@TableName(value = "user_video")
@Data
public class UserVideo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableField(value = "id")
    private Long id;

    @Schema(description = "用户唯一标识")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "视频标识")
    @TableField(value = "vid")
    private Long vid;

    @Schema(description = "播放次数")
    @TableField(value = "play")
    private Integer play;

    @Schema(description = "是否点赞")
    @TableField(value = "love")
    private Integer love;

    @Schema(description = "是否点踩")
    @TableField(value = "unlove")
    private Integer unlove;

    @Schema(description = "投币数量")
    @TableField(value = "coin")
    private Integer coin;

    @Schema(description = "是否收藏")
    @TableField(value = "collect")
    private Integer collect;

    @Schema(description = "播放时间")
    @TableField(value = "play_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date playTime;

    @Schema(description = "点赞时间")
    @TableField(value = "love_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loveTime;

    @Schema(description = "投币时间")
    @TableField(value = "coin_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date coinTime;

    @Schema(description = "收藏时间")
    @TableField(value = "collect_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date collectTime;
}

package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema(description = "用户其他数据表")
@TableName(value = "user_stats")
@Data
public class UserStats implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户唯一标识")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "粉丝数")
    @TableField(value = "fans_count")
    private Long fansCount;

    @Schema(description = "关注数")
    @TableField(value = "follow_count")
    private Integer followCount;

    @Schema(description = "获赞数")
    @TableField(value = "like_count")
    private Long likeCount;

    @Schema(description = "播放数")
    @TableField(value = "play_count")
    private Long playCount;

    @Schema(description = "投稿数")
    @TableField(value = "video_count")
    private Integer videoCount;

    @Schema(description = "合集数")
    @TableField(value = "collection_count")
    private Integer collectionCount;

    @Schema(description = "收藏数")
    @TableField(value = "collect_count")
    private Integer collectCount;

    @Schema(description = "出生日期")
    @TableField(value = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Schema(description = "学校信息")
    @TableField(value = "school")
    private String school;

    @Schema(description = "公告")
    @TableField(value = "notice")
    private String notice;

    @Schema(description = "置顶视频")
    @TableField(value = "masterpiece")
    private Long masterpiece;
}

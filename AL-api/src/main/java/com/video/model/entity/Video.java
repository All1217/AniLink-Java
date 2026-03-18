package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema(description = "视频表")
@TableName(value = "video")
@Data
public class Video implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "视频唯一标识")
    @TableId(type = IdType.AUTO)
    private Long vid;

    @Schema(description = "作者ID")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "标题")
    @TableField(value = "title")
    private String title;

    @Schema(description = "类型")
    @TableField(value = "type")
    private Integer type;

    @Schema(description = "作者声明")
    @TableField(value = "auth")
    private Integer auth;

    @Schema(description = "时长")
    @TableField(value = "duration")
    private Double duration;

    @Schema(description = "主分区ID")
    @TableField(value = "mc_id")
    private Long mcId;

    @Schema(description = "子分区ID")
    @TableField(value = "sc_id")
    private Long scId;

    @Schema(description = "标签")
    @TableField(value = "tags")
    private String tags;

    @Schema(description = "简介")
    @TableField(value = "descr")
    private String descr;

    @Schema(description = "封面url")
    @TableField(value = "cover_url")
    private String coverUrl;

    @Schema(description = "视频url")
    @TableField(value = "video_url")
    private String videoUrl;

    @Schema(description = "状态")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "可见状态")
    @TableField(value = "visible")
    private Integer visible;

    @Schema(description = "上传时间")
    @TableField(value = "upload_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadDate;

    @Schema(description = "删除时间")
    @TableField(value = "delete_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deleteDate;
}

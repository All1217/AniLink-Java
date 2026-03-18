package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema(description = "弹幕表")
@TableName(value = "danmu")
@Data
public class Danmu implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "视频ID")
    @TableField(value = "vid")
    private Long vid;

    @Schema(description = "用户ID")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "弹幕文本")
    @TableField(value = "content")
    private String content;

    @Schema(description = "字体大小")
    @TableField(value = "fontsize")
    private Integer fontsize;

    @Schema(description = "弹幕位置")
    @TableField(value = "mode")
    private Integer mode;

    @Schema(description = "字体颜色")
    @TableField(value = "color")
    private String color;

    @Schema(description = "出现时间点")
    @TableField(value = "time_point")
    private Double timePoint;

    @Schema(description = "弹幕状态")
    @TableField(value = "status")
    private Integer status;

    @Schema(description = "发送时间")
    @TableField(value = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
}

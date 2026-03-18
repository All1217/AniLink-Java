package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema(description = "收藏夹-视频关联表")
@TableName(value = "favorite_video")
@Data
public class FavoriteVideo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableField(value = "id")
    private Long id;
    @TableField(value = "fid")
    private Long fid;
    @TableField(value = "vid")
    private Long vid;
    @TableField(value = "time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    @TableField(value = "is_remove")
    private Integer isRemove;
}

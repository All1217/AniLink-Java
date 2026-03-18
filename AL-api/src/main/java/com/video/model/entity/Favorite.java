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

@Schema(description = "收藏夹表")
@TableName(value = "favorite")
@Data
public class Favorite implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long fid;
    @TableField(value = "uid")
    private Long uid;
    @TableField(value = "type")
    private Integer type;
    @TableField(value = "visible")
    private Integer visible;
    @TableField(value = "cover")
    private String cover;
    @TableField(value = "title")
    private String title;
    @TableField(value = "description")
    private String description;
    @TableField(value = "count")
    private Integer count;
    @TableField(value = "is_delete")
    private Integer isDelete;
}

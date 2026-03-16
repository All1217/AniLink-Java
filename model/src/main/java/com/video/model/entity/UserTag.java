package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description = "用户&标签关联表")
@TableName(value = "user_tag")
@Data
public class UserTag {
    @Schema(description = "主键")
    @TableField(value = "id")
    private Long id;

    @Schema(description = "用户唯一标识")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "标签名")
    @TableField(value = "tag_name")
    private String tagName;

    @Schema(description = "标签权重")
    @TableField(value = "weight")
    private Double weight;

    @Schema(description = "版本")
    @TableField(value = "version")
    private Integer version;

    @Schema(description = "创建时间")
    @TableField(value = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Schema(description = "修改时间")
    @TableField(value = "updated_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedDate;
}

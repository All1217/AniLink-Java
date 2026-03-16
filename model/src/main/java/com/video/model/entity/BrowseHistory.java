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

@Schema(description = "视频浏览历史表")
@TableName(value = "bh_video")
@Data
public class BrowseHistory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "视频ID")
    @TableField(value = "vid")
    private Long vid;

    @Schema(description = "观看时间")
    @TableField(value = "view_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date viewTime;

    @Schema(description = "观看时长")
    @TableField(value = "duration")
    private Double duration;

    @Schema(description = "是否完播")
    @TableField(value = "is_finish")
    private Integer isFinish;

    @Schema(description = "设备信息")
    @TableField(value = "device")
    private String device;

    @Schema(description = "IP地址")
    @TableField(value = "ip")
    private String ip;

    @Schema(description = "是否删除")
    @TableField(value = "is_delete")
    private Integer isDelete;
}

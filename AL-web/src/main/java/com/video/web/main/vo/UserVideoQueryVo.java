package com.video.web.main.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Getter
@Schema(description = "用户与视频交互Vo")
public class UserVideoQueryVo {
    @Schema(description = "主键")
    @TableField(value = "id")
    private Long id;

    @Schema(description = "用户唯一标识")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "视频标识")
    @TableField(value = "vid")
    private Long vid;

    /**
     @description: 原来的表达可能已弃，现其表达的意思根据具体调用的接口决定。
     点赞：0不点赞或取消点赞，1点赞。投币：0不投币或取消投币，1~2投币数量。
     当其作为返回值时，若为点赞相关，则0表示无点赞，1表示点赞。
     若为投币相关，则0表示无投币，1~2表示投币数量
     */
    @Schema(description = "交互类型")
    @TableField(value = "action_type")
    private Integer actionType;

    @Schema(description = "交互时间")
    @TableField(value = "action_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actionTime;
}

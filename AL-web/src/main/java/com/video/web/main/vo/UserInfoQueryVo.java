package com.video.web.main.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "用户基本信息请求参数")
public class UserInfoQueryVo {
    @Schema(description = "ID")
    @TableField(value = "uid")
    private Long uid;

    @Schema(description = "头像")
    @TableField(value = "avatar")
    private String avatar;

    @Schema(description = "个性签名")
    @TableField(value = "description")
    private String description;

    @Schema(description = "昵称")
    @TableField(value = "nickname")
    private String nickname;

    @Schema(description = "性别")
    @TableField(value = "gender")
    private Integer gender;

    @Schema(description = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "gender")
    private Date birthDate;

    @Schema(description = "学校信息")
    @TableField(value = "gender")
    private String school;
}

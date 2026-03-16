package com.video.web.main.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
@Schema(description = "用户信息Vo")
public class UserVo {
    @Schema(description = "主键")
    private Long uid;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个性签名")
    private String description;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthDate;

    @Schema(description = "学校信息")
    private String school;
}

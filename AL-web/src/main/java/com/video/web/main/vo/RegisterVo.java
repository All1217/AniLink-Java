package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "注册信息")
public class RegisterVo {
    @Schema(description="用户名")
    private String nickname;

    @Schema(description="密码")
    private String password;
}

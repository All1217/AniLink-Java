package com.video.web.main.controller.login;

import com.video.common.exception.VideoException;
import com.video.common.login.LoginUserHolder;
import com.video.common.result.Result;
import com.video.common.utils.WebUtils;
import com.video.model.entity.UserInfo;
import com.video.web.main.service.LoginService;
import com.video.web.main.vo.LoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.video.common.constant.JWTConstant.COOKIE_HEADER;
import static com.video.common.result.ResultCodeEnum.TOKEN_EXPIRED;

@Tag(name = "登录接口")
@RestController
@RequestMapping("/main")
public class LoginController {
    @Autowired
    private LoginService service;

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String jwt = service.login(loginVo);
        return Result.ok(jwt);
    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    public Result<UserInfo> info() {
        Long userId = LoginUserHolder.getLoginUser().getUserId();
        UserInfo userInfo = service.getLoginUserInfo(userId);
        return Result.ok(userInfo);
    }

    @Operation(summary = "刷新token")
    @GetMapping(value = "/public/refresh")
    public String refreshToken(@CookieValue(value = COOKIE_HEADER, required = false) String token) {
        if (token == null) {
            throw new VideoException(TOKEN_EXPIRED);
        }
        // TODO: 拿不到origin
//        String host = WebUtils.getHeader("origin");
//        if (host == null) {
//            throw new VideoException(TOKEN_EXPIRED);
//        }
        return service.refreshToken(WebUtils.cookieBuilder().decode(token));
    }
}

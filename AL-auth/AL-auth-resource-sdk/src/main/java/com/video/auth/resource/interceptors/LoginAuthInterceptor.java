package com.video.auth.resource.interceptors;

import com.video.common.login.LoginUser;
import com.video.common.login.LoginUserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.尝试获取用户信息
        LoginUser lu = LoginUserHolder.getLoginUser();
        if (lu == null) {
            response.setStatus(305);
            response.sendError(305, "未登录！");
            // 2.3.未登录，直接拦截
            return false;
        }
        Long userId = lu.getUserId();
        // 2.判断是否登录
        if (userId == null) {
            response.setStatus(305);
            response.sendError(305, "未登录！");
            return false;
        }
        // 3.登录则放行
        return true;
    }
}

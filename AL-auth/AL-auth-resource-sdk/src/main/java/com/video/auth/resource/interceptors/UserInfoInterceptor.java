package com.video.auth.resource.interceptors;

import com.video.common.login.LoginUser;
import com.video.common.login.LoginUserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.video.common.constant.JWTConstant.USER_ID;

@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头获取用户信息（由上游服务放入）
        String userID = request.getHeader(USER_ID);
        // 2.判断是否为空
        if (userID == null) {
            return true;
        }
        // 3. 存入ThreadLocal，供当前线程的业务逻辑使用
        try {
            Long userId = Long.valueOf(userID);
            LoginUserHolder.setLoginUser(new LoginUser(userId, userId.toString()));
            return true;
        } catch (NumberFormatException e) {
            log.error("用户身份信息格式不正确，{}, 原因：{}", userID, e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理用户信息
        LoginUserHolder.clear();
    }
}

package com.video.web.main.interceptor;

import com.video.common.login.LoginUser;
import com.video.common.login.LoginUserHolder;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.video.common.utils.JwtUtil;

import static com.video.common.constant.JWTConstant.*;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        Claims claims = JwtUtil.parseToken(token);
        Long userId = claims.get(JWT_PAYLOAD_USER_ID, Long.class);
        String userName = claims.get(JWT_PAYLOAD_USER_NAME, String.class);
        LoginUserHolder.setLoginUser(new LoginUser(userId, userName));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserHolder.clear();
    }
}

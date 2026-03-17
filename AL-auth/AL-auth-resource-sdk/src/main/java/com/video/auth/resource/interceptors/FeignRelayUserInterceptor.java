package com.video.auth.resource.interceptors;

import com.video.common.login.LoginUser;
import com.video.common.login.LoginUserHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import static com.video.common.constant.JWTConstant.USER_ID;

public class FeignRelayUserInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 1. 从ThreadLocal获取当前线程的用户信息
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (loginUser == null) {
            return;  // 用户不存在，不传递
        }
        Long userId = loginUser.getUserId();
        // 2. 将用户信息放入请求头
        template.header(USER_ID, userId.toString());
    }
}

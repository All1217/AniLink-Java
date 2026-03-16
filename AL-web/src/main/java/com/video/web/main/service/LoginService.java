package com.video.web.main.service;

import com.video.model.entity.UserInfo;
import com.video.web.main.vo.LoginVo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface LoginService {
    String login(LoginVo loginVo);

    UserInfo getLoginUserInfo(Long userId);

    String refreshToken(String refreshToken);
}

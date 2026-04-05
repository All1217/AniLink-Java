package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.exception.VideoException;
import com.video.common.login.LoginUser;
import com.video.common.login.LoginUserHolder;
import com.video.common.result.ResultCodeEnum;
import com.video.common.utils.JwtUtil;
import com.video.common.utils.StringUtils;
import com.video.common.utils.WebUtils;
import com.video.model.entity.UserInfo;
import com.video.model.enums.BaseStatus;
import com.video.web.main.mapper.UserInfoMapper;
import com.video.web.main.service.LoginService;
import com.video.web.main.vo.LoginVo;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.video.common.constant.JWTConstant.*;
//import org.apache.commons.codec.digest.DigestUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final UserInfoMapper userInfoMapper;
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate redisTemplate;

    @Override
    public String login(LoginVo loginVo) {
        //校验用户是否存在
        UserInfo user = userInfoMapper.loginOneByNickname(loginVo.getUsername());
        if (user == null) {
            throw new VideoException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }
        //校验用户是否被禁
        if (Objects.equals(user.getStatus(), BaseStatus.DISABLE.getCode())) {
            throw new VideoException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        } else if (Objects.equals(user.getStatus(), BaseStatus.LOGOUT.getCode())) {
            throw new VideoException(ResultCodeEnum.ADMIN_ACCOUNT_LOGOUT_ERROR);
        }
        //校验用户密码
        if (!user.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))) {
            throw new VideoException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }
        //发消息请求构建用户画像
        Map<String, Object> msg = new HashMap<>(2);
        msg.put("uid", user.getUid().toString());//python那边没法解析java的包装类Long
        msg.put("timestamp", System.currentTimeMillis());
        rabbitTemplate.convertAndSend("userProfile.exchange", "userProfile", msg);
        return geneTokens(user.getUid(), user.getUsername());
    }

    private String geneTokens(Long uid, String username) {
        //创建并返回TOKEN
        String accessToken = JwtUtil.createToken(uid, username);
        String refreshToken = JwtUtil.createRefreshToken(uid, redisTemplate);
        WebUtils.cookieBuilder()
                .name(COOKIE_HEADER)
                .value(refreshToken)
                .maxAge(REFRESH_TOKEN_EXPIRATION_DAY)
                .httpOnly(true)
                .build();
        return accessToken;
    }

    @Override
    public UserInfo getLoginUserInfo(Long userId) {
        return userInfoMapper.getById(userId);
    }

    @Override
    public String refreshToken(String refreshToken) {
        // 1.校验refresh-token,校验JTI
        Claims claims = JwtUtil.parseToken(refreshToken);
        Long userId = claims.get(JWT_PAYLOAD_USER_ID, Long.class);
        if (userId == null) {
            throw new VideoException(ResultCodeEnum.TOKEN_INVALID);
        }
        // 2.JTI校验
        Long payloadJti = claims.get(JWT_PAYLOAD_JTI, Long.class);
        String jti = redisTemplate.opsForValue().get(JWT_REDIS_KEY_PREFIX + userId);
        if (!StringUtils.equals(jti, payloadJti.toString())) {
            throw new VideoException(ResultCodeEnum.TOKEN_INVALID);
        }
        // 3.发MQ通知构建用户画像
        Map<String, Object> msg = new HashMap<>(2);
        msg.put("uid", userId.toString());//python那边没法解析java的包装类Long
        msg.put("timestamp", System.currentTimeMillis());
        rabbitTemplate.convertAndSend("userProfile.exchange", "userProfile", msg);
        // 4.生成新的access-token、refresh-token
        return geneTokens(userId, Long.toString(userId));
    }

    @Override
    public void logout() {
        WebUtils.cookieBuilder()
                .name(COOKIE_HEADER)
                .value("")
                .maxAge(0)
                .httpOnly(true)
                .build();
        JwtUtil.clearJti(redisTemplate);
    }
}
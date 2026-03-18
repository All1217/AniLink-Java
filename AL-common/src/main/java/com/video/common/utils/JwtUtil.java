package com.video.common.utils;

import com.video.common.exception.VideoException;
import com.video.common.login.LoginUserHolder;
import com.video.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

import static com.video.common.constant.JWTConstant.*;

@Slf4j
public class JwtUtil {
    private static final SecretKey tokenSignKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    public static String createToken(Long userId, String username) {
        return Jwts.builder().
                setSubject("USER_INFO").
                setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION)).
                claim(JWT_PAYLOAD_USER_ID, userId).
                claim(JWT_PAYLOAD_USER_NAME, username).
                signWith(tokenSignKey).
                compact();
    }

    public static String createRefreshToken(Long userId, StringRedisTemplate template) {
        long jti = CodeUtil.geneVisibleCode(userId, System.currentTimeMillis());
        Duration ttl = Duration.ofMillis(REFRESH_TOKEN_EXPIRATION);
        String token = Jwts.builder().
                setSubject("USER_INFO").
                setExpiration(new Date(System.currentTimeMillis() + ttl.toMillis())).
                claim(JWT_PAYLOAD_USER_ID, userId).
                claim(JWT_PAYLOAD_JTI, jti).
                signWith(tokenSignKey).
                compact();
        // 缓存jti，有效期与token一致，过期或删除JTI后，对应的refresh-token失效
        template.opsForValue()
                .set(JWT_REDIS_KEY_PREFIX + userId, Long.toString(jti), ttl);
        return token;
    }

    public static Claims parseToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new VideoException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(tokenSignKey).build();
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            throw new VideoException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new VideoException(ResultCodeEnum.TOKEN_INVALID);
        }
    }

    public static void clearJti(StringRedisTemplate template) {
        log.error("common模块用户信息：{}", LoginUserHolder.getLoginUser());
        if (LoginUserHolder.getLoginUser() == null || template == null) {
            return;
        }
        template.delete(JWT_REDIS_KEY_PREFIX + LoginUserHolder.getLoginUser().getUserId());

    }

    public static void main(String[] args) {
        System.out.println(createToken(123123123L, "123123123"));
//        System.out.println(parseToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVU0VSX0lORk8iLCJleHAiOjE3NzM1MTc4MDksInVzZXJJZCI6MSwidXNlcm5hbWUiOiIxMjMxMjMxMjMifQ.QbedJJtDmYTwHUi3S3aW_NSRM_rxC30cJS2hbXFVgT8"));
    }
}

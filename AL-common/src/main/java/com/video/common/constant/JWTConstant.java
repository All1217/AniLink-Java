package com.video.common.constant;

public class JWTConstant {
    public static final String AUTHORIZATION_HEADER = "access-token";
    public static final String COOKIE_HEADER = "refresh-token";
    public static final String USER_ID = "user-id";
    public static final String JWT_PAYLOAD_USER_ID = "userId";
    public static final String JWT_PAYLOAD_JTI = "jti";
    public static final String JWT_PAYLOAD_USER_NAME= "username";
    // jti的key
    public static final String JWT_REDIS_KEY_PREFIX = "jwt:uid:";
    public final static long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000 * 12L;//单位是毫秒，12小时
//    public final static long ACCESS_TOKEN_EXPIRATION = 5 * 1000L;//测试用
    public final static long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 1000 * 24 * 3L;//单位是毫秒，3天
//    public final static long REFRESH_TOKEN_EXPIRATION = 15 * 1000L;//测试用
    // refresh-token的过期时间，单位天
    public final static int REFRESH_TOKEN_EXPIRATION_DAY = 3;
}

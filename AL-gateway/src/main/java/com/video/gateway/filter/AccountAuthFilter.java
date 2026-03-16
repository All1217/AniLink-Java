package com.video.gateway.filter;

import com.video.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.video.common.constant.JWTConstant.*;

// TODO: 网关层没有全局异常处理器
@Component
public class AccountAuthFilter implements GlobalFilter, Ordered {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final Set<String> excludePath = new HashSet<>();

    {
        excludePath.add("/doc.html");
        excludePath.add("/main/login/**");
        excludePath.add("/main/user/register");
        excludePath.add("/main/video/home/**");
        excludePath.add("/main/**/public/**");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取请求request信息
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        // 2.判断是否是无需登录的路径
        if (isExcludePath(path)) {
            // 直接放行
            return chain.filter(exchange);
        }
        // 3.尝试获取token
        List<String> authHeaders = exchange.getRequest().getHeaders().get(AUTHORIZATION_HEADER);
        String token = authHeaders == null ? "" : authHeaders.get(0);
        Claims claims = JwtUtil.parseToken(token);
        Long userId = claims.get(JWT_PAYLOAD_USER_ID, Long.class);
        // TODO: 这里的userName解析出来是空值，待深入处理。可能和JWT官方类型转换器有关，官方默认的转换器无法解析字符串
//        String userName = claims.get(JWT_PAYLOAD_USER_NAME, String.class);
        // 4.如果用户是登录状态，尝试更新请求头，传递用户信息
        exchange.mutate()
                .request(builder -> builder.header(USER_ID, userId.toString()))
                .build();
        // TODO: 因为拦截器在网关层不生效，所以需要另外想个办法存储用户信息到ThreadLocal
        // 5.放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isExcludePath(String antPath) {
        for (String pathPattern : this.excludePath) {
            if (antPathMatcher.match(pathPattern, antPath)) {
                return true;
            }
        }
        return false;
    }
}

package com.video.gateway.exception;

import cn.hutool.json.JSONUtil;
import com.video.common.exception.VideoException;
import com.video.common.result.Result;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.video.common.result.ResultCodeEnum.APP_SERVER_ERROR;
import static com.video.common.result.ResultCodeEnum.FAIL;

/**
 * TODO: 有改进空间
* */
@Component
public class GatewayExceptionHandler implements ErrorWebExceptionHandler, Ordered {
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 1.获取响应
        ServerHttpResponse response = exchange.getResponse();
        // 2.判断是否已处理
        if (response.isCommitted()) {
            // 如果已经提交，直接结束，避免重复处理
            return Mono.error(ex);
        }
        // 3.按照异常类型进行翻译处理，翻译的结果易于前端理解
        String message;
        int code = FAIL.getCode();
        if (ex instanceof VideoException e) {
            // 登录异常
            Result<Object> r = Result.fail(e.getCode(), e.getMessage());
            byte[] resp = JSONUtil.toJsonStr(r).getBytes(StandardCharsets.UTF_8);
            return response.writeWith(
                    Mono.fromSupplier(
                            () -> response.bufferFactory().wrap(resp)
                    ));
        } else if (ex instanceof NotFoundException) {
            message = "服务不存在";
        } else if (ex instanceof ResponseStatusException) {
            message = ex.getMessage();
        } else {
            message = APP_SERVER_ERROR.getMessage();
        }
        // 5.设置响应结果为 JSON
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 6.封装响应结果并写出
        Result<Object> r = Result.fail(code, message);
        List<String> requestIds = response.getHeaders().get("requestId");
//        if (requestIds != null) {
//            r.requestId(requestIds.get(0));
//        }
        byte[] resp = JSONUtil.toJsonStr(r).getBytes(StandardCharsets.UTF_8);
        return response.writeWith(
                Mono.fromSupplier(
                        () -> response.bufferFactory().wrap(resp)
                ));
    }
}

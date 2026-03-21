package com.video.communication.websocket;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionCloseHandler extends ChannelDuplexHandler {

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        log.debug("主动关闭连接: {}", ctx.channel().remoteAddress());
        super.close(ctx, promise);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("连接已关闭: {}", ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof java.io.IOException) {
            String message = cause.getMessage();
            if (message != null &&
                    (message.contains("远程主机强迫关闭了一个现有的连接") ||
                            message.contains("Connection reset by peer") ||
                            message.contains("你的主机中的软件中止了一个已建立的连接") ||
                            message.contains("Broken pipe"))) {
                // 客户端主动断开连接，这是正常情况
                log.debug("客户端主动断开连接: {}", ctx.channel().remoteAddress());
                ctx.close();
                return;
            }
        }
        // 其他异常才打印错误日志
        log.error("连接异常: {}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
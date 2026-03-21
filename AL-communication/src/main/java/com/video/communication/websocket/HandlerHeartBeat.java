package com.video.communication.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class HandlerHeartBeat extends ChannelDuplexHandler {
    // 定义 AttributeKey 常量
    private static final AttributeKey<String> USER_ID = AttributeKey.valueOf("userId");
    private static final AttributeKey<AtomicInteger> HEARTBEAT_COUNT = AttributeKey.valueOf("heartbeatCount");
    // 心跳超时次数
    private static final int MAX_HEARTBEAT_FAIL = 3;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            Channel channel = ctx.channel();

            if (e.state() == IdleState.READER_IDLE) {
                // 读空闲：客户端长时间未发送数据
                handleReaderIdle(channel, ctx);
            } else if (e.state() == IdleState.WRITER_IDLE) {
                // 写空闲：服务端长时间未发送数据
                handleWriterIdle(channel, ctx);
            } else if (e.state() == IdleState.ALL_IDLE) {
                // 所有空闲：读写都空闲
                handleAllIdle(channel, ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 处理读空闲（客户端未发送数据）
     * 策略：记录心跳失败次数，超过阈值则关闭连接
     */
    private void handleReaderIdle(Channel channel, ChannelHandlerContext ctx) {
        String userId = channel.attr(USER_ID).get();
        AtomicInteger failCount = channel.attr(HEARTBEAT_COUNT).get();
        if (failCount == null) {
            failCount = new AtomicInteger(0);
            channel.attr(HEARTBEAT_COUNT).set(failCount);
        }
        int count = failCount.incrementAndGet();
        if (count >= MAX_HEARTBEAT_FAIL) {
            log.warn("用户 {} 心跳超时次数达到 {} 次，关闭连接", userId, MAX_HEARTBEAT_FAIL);
            ctx.close();
        } else {
            log.debug("用户 {} 读空闲 {} 次，发送心跳请求", userId, count);
            // 发送心跳请求给客户端
            ctx.writeAndFlush(new TextWebSocketFrame("ping"));
        }
    }

    /**
     * 处理写空闲（服务端未发送数据）
     * 策略：发送心跳保持连接
     */
    private void handleWriterIdle(Channel channel, ChannelHandlerContext ctx) {
        String userId = channel.attr(USER_ID).get();
        log.trace("用户 {} 写空闲，发送心跳", userId);

        // 发送 WebSocket 格式的心跳
        ctx.writeAndFlush(new TextWebSocketFrame("ping"));
    }

    /**
     * 处理所有空闲
     */
    private void handleAllIdle(Channel channel, ChannelHandlerContext ctx) {
        String userId = channel.attr(USER_ID).get();
        log.debug("用户 {} 连接全空闲", userId);
        // 可以根据业务需求决定是否关闭
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接建立时的处理
        log.info("新连接建立: {}", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 连接关闭时的处理
        String userId = ctx.channel().attr(USER_ID).get();
        log.info("连接关闭: {}, 用户: {}", ctx.channel().remoteAddress(), userId);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("WebSocket 连接异常", cause);
        ctx.close();
    }
}
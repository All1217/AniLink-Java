package com.video.communication.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    protected String path;

    public BaseWebSocketHandler(String path) {
        this.path = path;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String message = ((TextWebSocketFrame) frame).text();
            handleMessage(ctx, message);
        }
    }

    protected abstract void handleMessage(ChannelHandlerContext ctx, String message);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("新连接加入 - 路径: {}, 地址: {}", path, ctx.channel().remoteAddress());
        onConnect(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("连接断开 - 路径: {}, 地址: {}", path, ctx.channel().remoteAddress());
        onDisconnect(ctx);
    }

    protected void onConnect(ChannelHandlerContext ctx) {
    }

    protected void onDisconnect(ChannelHandlerContext ctx) {
    }
}

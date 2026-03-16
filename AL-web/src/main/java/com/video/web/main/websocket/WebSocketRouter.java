package com.video.web.main.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;

@ChannelHandler.Sharable
public class WebSocketRouter extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            String uri = request.uri();
            ChannelPipeline pipeline = ctx.pipeline();

            // 将 URI 存入 Channel 的属性中
            ctx.channel().attr(AttributeKey.valueOf("requestUri")).set(uri);

            if (uri.startsWith("/ws/video")) {
                pipeline.addLast(new WebSocketServerProtocolHandler("/ws/video",
                        null, true, 64 * 1024,
                        true, true, 10000L));
                pipeline.addLast(new HandlerVideo());
            } else if (uri.startsWith("/ws/chat")) {
                pipeline.addLast(new WebSocketServerProtocolHandler("/ws/chat",
                        null, true, 64 * 1024,
                        true, true, 10000L));
                pipeline.addLast(new HandlerChat());
            } else {
                ctx.close();
                return;
            }

            pipeline.remove(this); // 移除自身，后续请求不再经过此路由器
            ctx.fireChannelRead(msg); // 继续传递
        } else {
            super.channelRead(ctx, msg);
        }
    }
}

package com.video.web.main.websocket;

import com.video.common.utils.WSUrlUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class HandlerVideo extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Map<String, Map<String, Channel>> userSessions = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有新的视频连接加入 .... ");

        Channel channel = ctx.channel();
        // 从 Channel 中获取之前存储的请求 URI
        String requestUri = (String) channel.attr(AttributeKey.valueOf("requestUri")).get();
        log.info("传入工具前：{}", requestUri);
        WSUrlUtil urlUtil = new WSUrlUtil(requestUri);

        String vid = urlUtil.getVid();
        String uid = urlUtil.getUid();

        userSessions.putIfAbsent(vid, new HashMap<>());
        if (userSessions.get(vid).containsKey(uid)) {
            log.info("用户{}已经在观看视频{}！", uid, vid);
        } else {
            userSessions.get(vid).put(uid, channel);
            log.info("用户{}观看视频{}", uid, vid);
            broadcastOnlineCount(vid);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("有视频连接断开 .... ");

        Channel channel = ctx.channel();
        String requestUri = (String) channel.attr(AttributeKey.valueOf("requestUri")).get();
        log.info("requestUri：{}", requestUri);
        WSUrlUtil urlUtil = new WSUrlUtil(requestUri);
        String vid = urlUtil.getVid();
        String uid = urlUtil.getUid();

        if (userSessions.containsKey(vid)) {
            userSessions.get(vid).remove(uid);
            log.info("用户{}停止观看视频{}", uid, vid);
            if (userSessions.get(vid).isEmpty()) {
                userSessions.remove(vid);
                log.info("视频{}无人观看，清除ws连接", vid);
            } else {
                broadcastOnlineCount(vid);
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = ctx.channel();
        // 从 ChannelHandlerContext 中获取 WebSocket 请求的 URL
        String requestUri = (String) channel.attr(AttributeKey.valueOf("requestUri")).get();
        WSUrlUtil urlUtil = new WSUrlUtil(requestUri);
        String vid = urlUtil.getVid();
        String uid = urlUtil.getUid();
        String message = textWebSocketFrame.text();
        log.info("收到{}的消息: {}", uid, message);
        if (userSessions.containsKey(vid) && !userSessions.get(vid).isEmpty()) {
            broadcastMessage(message, vid, uid);
        }
    }

    private void broadcastOnlineCount(String vid) {
        int count = userSessions.get(vid).size();
        String message = String.format("{\"type\": false, \"cnt\": %d}", count);
        broadcastToAllUsers(vid, message);
    }

    private void broadcastMessage(String message, String vid, String uid) {
        String formattedMessage = String.format("{\"type\": true, \"message\": \"%s\"}", message);
        broadcastToAllUsers(vid, formattedMessage, uid);
    }

    private void broadcastToAllUsers(String vid, String message) {
        for (Channel channel : userSessions.get(vid).values()) {
            channel.writeAndFlush(new TextWebSocketFrame(message));
        }
    }

    private void broadcastToAllUsers(String vid, String message, String excludeUid) {
        for (Map.Entry<String, Channel> entry : userSessions.get(vid).entrySet()) {
            if (!entry.getKey().equals(excludeUid)) {
                entry.getValue().writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }
}

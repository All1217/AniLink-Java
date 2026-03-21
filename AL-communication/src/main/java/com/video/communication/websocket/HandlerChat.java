package com.video.communication.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.common.utils.JsonUtils;
import com.video.communication.domain.dto.ChatMessageDTO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class HandlerChat extends BaseWebSocketHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // 用户ID与Channel的映射
    private static final ConcurrentHashMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    // Channel与用户ID的映射
    private static final ConcurrentHashMap<Channel, String> channelUserMap = new ConcurrentHashMap<>();

    public HandlerChat(String path) {
        super(path);
    }

    @Override
    protected void handleMessage(ChannelHandlerContext ctx, String message) {
        try {
            JsonNode json = objectMapper.readTree(message);
            ChatMessageDTO data = JsonUtils.toBean(json.toString(), ChatMessageDTO.class);
            String type = data.getType();
            switch (type) {
                case "auth":
                    // 用户认证
                    authenticate(ctx.channel(), data.getFrom());
                    break;
                case "private":
                    // 私聊消息
                    sendPrivateMessage(ctx.channel(), data.getTo(), data.getContent());
                    break;
                case "heartbeat":
                    ctx.channel().writeAndFlush(new TextWebSocketFrame("{\"type\":\"success\"}"));
                    break;
                default:
                    log.warn("未知消息类型: {}", type);
            }
        } catch (Exception e) {
            log.error("处理消息失败", e);
        }
    }

    private void authenticate(Channel channel, String userId) {
        // 保存用户连接
        userChannelMap.put(userId, channel);
        channelUserMap.put(channel, userId);
        // 发送认证成功响应
        ChatMessageDTO msg = new ChatMessageDTO();
        msg.setType("success");
        msg.setFrom(userId);
        msg.setTo(userId);
        channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.toJsonStr(msg)));
        log.info("用户认证成功 - userId: {}", userId);
    }

    private void sendPrivateMessage(Channel fromChannel, String toUserId, String content) {
        String fromUserId = channelUserMap.get(fromChannel);
        if (fromUserId == null) {
            return;
        }
        Channel toChannel = userChannelMap.get(toUserId);
        if (toChannel == null || !toChannel.isActive()) {
            // 用户不在线
            return;
        }
        // 构建消息
        ChatMessageDTO msg = new ChatMessageDTO();
        msg.setType("private");
        msg.setFrom(fromUserId);
        msg.setTo(toUserId);
        msg.setContent(content);
        toChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.toJsonStr(msg)));
    }

    @Override
    protected void onDisconnect(ChannelHandlerContext ctx) {
        String userId = channelUserMap.remove(ctx.channel());
        if (userId != null) {
            userChannelMap.remove(userId);
            log.info("用户离线 - userId: {}", userId);
        }
    }

    /**
     * 检查用户是否在线（供外部调用）
     */
    public static boolean isUserOnline(String userId) {
        Channel channel = userChannelMap.get(userId);
        return channel != null && channel.isActive();
    }
}
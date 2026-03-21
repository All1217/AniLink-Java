package com.video.communication.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.common.utils.JsonUtils;
import com.video.communication.domain.dto.CountMessageDTO;
import com.video.communication.domain.vo.CountMessageVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ConcurrentHashMap;

// TODO: 存在同一个用户反复打开同一个视频刷在线人数的漏洞
@Slf4j
public class HandlerVideo extends BaseWebSocketHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 按视频ID分组管理连接
    private static final ConcurrentHashMap<String, ChannelGroup> videoViewers = new ConcurrentHashMap<>();

    // 记录每个连接对应的视频ID
    private static final ConcurrentHashMap<Channel, String> channelVideoMap = new ConcurrentHashMap<>();

    public HandlerVideo(String path) {
        super(path);
    }

    @Override
    protected void handleMessage(ChannelHandlerContext ctx, String message) {
        try {
            JsonNode json = objectMapper.readTree(message);
            CountMessageDTO data = JsonUtils.toBean(json.toString(), CountMessageDTO.class);
            String type = data.getType();
            switch (type) {
                case "join":
                    // 加入视频房间
                    String videoId = data.getVid();
                    joinVideoRoom(ctx.channel(), videoId);
                    break;
                case "heartbeat":
                    // 心跳，保持连接
                    ctx.channel().writeAndFlush(new TextWebSocketFrame("{\"type\":\"success\"}"));
                    break;
                default:
                    log.warn("未知消息类型: {}", type);
            }
        } catch (Exception e) {
            log.error("处理消息失败", e);
        }
    }

    private void joinVideoRoom(Channel channel, String videoId) {
        // 先离开之前的房间
        leaveVideoRoom(channel);
        // 获取或创建视频房间的 ChannelGroup
        ChannelGroup group = videoViewers.computeIfAbsent(videoId,
                k -> new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
        // 添加连接到房间
        group.add(channel);
        channelVideoMap.put(channel, videoId);
        // 获取当前观看人数
        int viewerCount = group.size();
        // 广播人数更新给房间内所有用户
        CountMessageVO cnt = new CountMessageVO();
        cnt.setCount(viewerCount);
        cnt.setType("cnt");
        group.writeAndFlush(new TextWebSocketFrame(JsonUtils.toJsonStr(cnt)));
        log.info("用户加入视频房间 - videoId: {}, 当前人数: {}, 连接数: {}",
                videoId, viewerCount, group.size());
    }

    private void leaveVideoRoom(Channel channel) {
        String videoId = channelVideoMap.remove(channel);
        if (videoId != null) {
            ChannelGroup group = videoViewers.get(videoId);
            if (group != null) {
                group.remove(channel);
                // 更新人数
                int viewerCount = group.size();
                CountMessageVO cnt = new CountMessageVO();
                cnt.setCount(viewerCount);
                cnt.setType("cnt");
                group.writeAndFlush(new TextWebSocketFrame(JsonUtils.toJsonStr(cnt)));
                // 如果房间为空，清理资源
                if (group.isEmpty()) {
                    videoViewers.remove(videoId);
                    log.info("视频房间已清空 - videoId: {}", videoId);
                }
                log.info("用户离开视频房间 - videoId: {}, 剩余人数: {}", videoId, viewerCount);
            }
        }
    }

    @Override
    protected void onDisconnect(ChannelHandlerContext ctx) {
        leaveVideoRoom(ctx.channel());
    }
    /**
     * 获取视频的当前观看人数（供外部调用）
     */
    public static int getViewerCount(String videoId) {
        ChannelGroup group = videoViewers.get(videoId);
        return group != null ? group.size() : 0;
    }
}
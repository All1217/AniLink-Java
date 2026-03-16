package com.video.web.main.websocket;

import com.alibaba.fastjson.JSON;
import com.video.model.entity.VideoOnlineMessage;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
@ServerEndpoint(value = "/ws/video/{vid}/{uid}")
public class VideoEndpoint {
    /**
     * @description: 使用Map<String, Map < String, Session>>记录当前已建立连接的用户。
     * 外层HashMap的键为视频vid，内层HashMap的键为用户uid，值为用户对应的session
     */
    private static final Map<String, Map<String, Session>> maps = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("vid") String vid, @PathParam("uid") String uid) {
        // 创建该视频的映射
        maps.putIfAbsent(vid, new HashMap<>());
        log.info("建立视频结点{}", vid);
        if (maps.get(vid).containsKey(uid)) {
            log.info("用户{}已经在观看视频{}！", uid, vid);
        } else {
            maps.get(vid).putIfAbsent(uid, session);
            log.info("用户{}观看视频{}", uid, vid);
        }
        if (!maps.get(vid).isEmpty()) {
            //广播在线观看人数加一
            VideoOnlineMessage send = new VideoOnlineMessage();
            send.setType(false);
            send.setCnt(maps.get(vid).size());
            broadcastAllUser(JSON.toJSONString(send), "0", vid);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        VideoOnlineMessage come = JSON.parseObject(message, VideoOnlineMessage.class);
        String uid = come.getUid();
        String vid = come.getVid();
        String text = come.getMessage();
        log.info("用户{}在视频{}发送了弹幕:{}", uid, vid, text);
        if (!maps.get(vid).isEmpty()) {
            VideoOnlineMessage send = new VideoOnlineMessage();
            send.setType(true);
            send.setMessage(text);
            broadcastAllUser(JSON.toJSONString(send), uid, vid);
        }
    }

    private void broadcastAllUser(String message, String uid, String vid) {
        Set<Map.Entry<String, Session>> entries = maps.get(vid).entrySet();
        for (Map.Entry<String, Session> entry : entries) {
            log.info("遍历到用户{}，{}", entry.getKey(), Objects.equals(entry.getKey(), uid));
            if (!Objects.equals(entry.getKey(), uid)) {
                Session session = entry.getValue();
                try {
                    log.info("给用户{}广播弹幕", entry.getKey());
                    session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClose
    public void onClose(@PathParam("vid") String vid, @PathParam("uid") String uid) {
        maps.get(vid).remove(uid);
        log.info("用户{}停止观看视频{}", uid, vid);
        if (maps.get(vid).isEmpty()) {
            maps.remove(vid);
            log.info("视频{}无人观看，清除ws连接", vid);
        } else {
            //广播：该视频在线观看人数减少1
            VideoOnlineMessage send = new VideoOnlineMessage();
            send.setType(false);
            send.setCnt(maps.get(vid).size());
            broadcastAllUser(JSON.toJSONString(send), uid, vid);
        }
    }
}

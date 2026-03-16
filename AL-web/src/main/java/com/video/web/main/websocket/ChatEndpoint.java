package com.video.web.main.websocket;

import com.alibaba.fastjson.JSON;
import com.video.model.entity.ChatOnlineMessage;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint(value = "/ws/{uid}")
public class ChatEndpoint {
    private static final Map<String, Session> userChats = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        // 创建该用户的聊天映射
        userChats.putIfAbsent(uid, session);
        log.info("用户【{}】已上线", uid);
    }

    private Set<String> getFriends() {
        return userChats.keySet();
    }

    @OnMessage
    public void onMessage(String message) {
        ChatOnlineMessage come = JSON.parseObject(message, ChatOnlineMessage.class);
        String fromName = come.getFrom();
        String toName = come.getTo();
        Session session;
        log.info("用户{},给{},发送了消息:{}", fromName, toName, come.getMessage());
        if (userChats.containsKey(toName)) {
            session = userChats.get(toName);
        } else {
            log.info("该用户不存在或已下线");
            return;
        }
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.info("error: {}", e);
        }
    }

    private void broadcastAllUser(String message) {
        Set<Map.Entry<String, Session>> entries = userChats.entrySet();
        for (Map.Entry<String, Session> entry : entries) {
            Session session = entry.getValue();
            //发送消息
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(@PathParam("uid") String uid) {
        userChats.remove(uid);
        log.info("用户【{}】已下线", uid);
    }
}
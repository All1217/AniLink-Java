package com.video.web.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Chat;
import com.video.model.entity.ChatMessage;
import com.video.web.main.vo.ChatQueryVo;
import com.video.web.main.vo.ChatVo;
import org.apache.ibatis.annotations.Insert;

public interface ChatService {
    Chat getById(Long i);

    IPage<ChatVo> getChatVoPage(IPage<ChatVo> page, Long uid);

    void deleteChat(Long senderId, Long targetId);

    IPage<ChatMessage> getChatMessagePage(IPage<ChatMessage> page, Long senderId, Long targetId);

    ChatVo insertOneChat(Chat chat);

    void insertOneChatMessage(ChatMessage cm);

    ChatVo getChatVoByST(Long senderId, Long targetId);

    void update(ChatQueryVo q);

    void updateUnread(ChatQueryVo q);

    Integer getWhisperUnread(ChatQueryVo q);
}

package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Chat;
import com.video.model.entity.ChatMessage;
import com.video.web.main.mapper.ChatMapper;
import com.video.web.main.service.ChatService;
import com.video.web.main.vo.ChatQueryVo;
import com.video.web.main.vo.ChatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;

    @Override
    public Chat getById(Long i) {
        return chatMapper.selectById(i);
    }

    @Override
    @Transactional
    public IPage<ChatVo> getChatVoPage(IPage<ChatVo> page, Long uid) {
        return chatMapper.getChatVoPage(page, uid);
    }

    @Override
    @Transactional
    public void deleteChat(Long senderId, Long targetId) {
        chatMapper.deleteChat(senderId, targetId);
        //删除相关聊天信息
        chatMapper.deleteChatMessage(senderId, targetId);
    }

    @Override
    public IPage<ChatMessage> getChatMessagePage(IPage<ChatMessage> page, Long senderId, Long targetId) {
        return chatMapper.getChatMessagePage(page, senderId, targetId);
    }

    @Override
    @Transactional
    public ChatVo insertOneChat(Chat chat) {
        Chat t = chatMapper.getByTwoIds(chat.getSenderId(), chat.getTargetId());
        ChatVo res;
        if (t == null) {
            chatMapper.insert(chat);
            res = chatMapper.getChatVoByTwoIds(chat.getSenderId(), chat.getTargetId());
        } else {
            res = chatMapper.getChatVoByTwoIds(t.getSenderId(), t.getTargetId());
        }
        return res;
    }

    @Override
    public void insertOneChatMessage(ChatMessage cm) {
        chatMapper.insertOneChatMessage(cm);
    }

    @Override
    public ChatVo getChatVoByST(Long senderId, Long targetId) {
        return chatMapper.getChatVoByTwoIds(senderId, targetId);
    }

    @Override
    public void update(ChatQueryVo q) {
        UpdateWrapper<Chat> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", q.getId());
        Chat newChat = new Chat();
        if (q.getTime() != null) newChat.setLatestTime(q.getTime());
        chatMapper.update(newChat, updateWrapper);
    }

    @Override
    public void updateUnread(ChatQueryVo q) {
        chatMapper.updateUnread(q);
    }

    @Override
    public Integer getWhisperUnread(ChatQueryVo q) {
        return chatMapper.getWhisperUnread(q);
    }
}

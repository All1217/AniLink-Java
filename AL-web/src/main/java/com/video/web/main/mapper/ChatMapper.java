package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Chat;
import com.video.model.entity.ChatMessage;
import com.video.web.main.vo.ChatQueryVo;
import com.video.web.main.vo.ChatVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ChatMapper extends BaseMapper<Chat> {

    IPage<ChatVo> getChatVoPage(IPage<ChatVo> page, Long uid);

    @Delete("delete from chat where (sender_id=#{senderId} and target_id = #{targetId}) " +
            "or (sender_id=#{targetId} and target_id = #{senderId})")
    void deleteChat(Long senderId, Long targetId);

    @Delete("delete from chat_message where (sender_id = #{senderId} and target_id =#{targetId}) " +
            "or (sender_id = #{targetId} and target_id =#{senderId})")
    void deleteChatMessage(Long senderId, Long targetId);

    IPage<ChatMessage> getChatMessagePage(IPage<ChatMessage> page, Long senderId, Long targetId);

    @Select("select * from chat " +
            "where (sender_id=#{senderId} and target_id = #{targetId}) " +
            "or (sender_id=#{targetId} and target_id = #{senderId})")
    Chat getByTwoIds(Long senderId, Long targetId);

    ChatVo getChatVoByTwoIds(Long senderId, Long targetId);

    void insertOneChatMessage(ChatMessage cm);

    @Update("update chat_message set read_status = 1 " +
            "where ((sender_id=#{senderId} and target_id=#{targetId})or(sender_id=#{targetId} and target_id=#{senderId})) " +
            "and target_id = #{uid}")
    void updateUnread(ChatQueryVo q);

    @Select("SELECT COUNT(1) FROM chat_message WHERE target_id = #{uid} AND read_status = 0;")
    Integer getWhisperUnread(ChatQueryVo q);
}

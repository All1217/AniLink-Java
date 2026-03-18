package com.video.web.main.mq;

import com.video.model.dto.remark.LikedTimesDTO;
import com.video.model.entity.VideoStats;
import com.video.web.main.service.VideoStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.video.common.constant.MqConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikeTimesChangeListener {
    private final VideoStatsService replyService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "video.liked.times.queue", durable = "true"),
            exchange = @Exchange(name = LIKE_RECORD_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = VIDEO_LIKED_TIMES_KEY
    ))
    public void listenReplyLikedTimesChange(List<LikedTimesDTO> likedTimesDTOs) {
//        log.debug("监听到回答或评论的点赞数变更");
        List<VideoStats> list = new ArrayList<>(likedTimesDTOs.size());
        for (LikedTimesDTO dto : likedTimesDTOs) {
            VideoStats r = new VideoStats();
            r.setVid(dto.getBizId());
            r.setGood(dto.getLikedTimes().longValue());
            list.add(r);
        }
        replyService.updateBatchById(list);
    }
}

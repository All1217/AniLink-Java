package com.video.remark.task;

import com.video.remark.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import static com.video.common.constant.LikeBizTypeEnum.COMMENT_LIKES;
import static com.video.common.constant.LikeBizTypeEnum.VIDEO_LIKES;

@Component
@RequiredArgsConstructor
public class LikedTimesCheckTask {
    private static final Byte[] BIZ_TYPES = {VIDEO_LIKES.getCode(), COMMENT_LIKES.getCode()};
    private static final int MAX_BIZ_SIZE = 30;

    private final LikeService likeService;

    @Scheduled(fixedDelay = 20000)
    public void checkLikedTimes() {
        for (Byte bizType : BIZ_TYPES) {
            likeService.readLikedTimesAndSendMessage(bizType, MAX_BIZ_SIZE);
        }
    }
}

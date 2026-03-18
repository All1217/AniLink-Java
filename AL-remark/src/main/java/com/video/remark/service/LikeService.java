package com.video.remark.service;

import com.video.common.result.Result;
import com.video.remark.domain.LikeDTO;

public interface LikeService {
    void doLike(LikeDTO likeDTO);

    void readLikedTimesAndSendMessage(Byte bizType, int maxBizSize);

    Result<Integer> getLikeState(Long bizId);
}

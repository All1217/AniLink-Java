package com.video.common.constant;

import lombok.Getter;

@Getter
public enum LikeBizTypeEnum {
    VIDEO_LIKES(Byte.valueOf("0"), "视频点赞"),
    COMMENT_LIKES(Byte.valueOf("1"), "评论/回复点赞");

    private final Byte code;

    private final String des;

    LikeBizTypeEnum(Byte code, String des) {
        this.code = code;
        this.des = des;
    }
}

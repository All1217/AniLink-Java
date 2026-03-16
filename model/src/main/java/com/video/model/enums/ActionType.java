package com.video.model.enums;

import lombok.Getter;

@Getter
public enum ActionType {
    LIKE(0, "点赞"),
    DISLIKE(1, "点踩"),
    COIN_ONE(2, "投币1"),
    COIN_TWO(3, "投币2"),
    COLLECT(4, "收藏"),
    SHARE(5, "分享"),
    PLAY(6, "播放");

    private final Integer num;

    private final String name;

    ActionType(Integer num, String name) {
        this.num = num;
        this.name = name;
    }
}

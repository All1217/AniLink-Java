package com.video.model.enums;

import lombok.Getter;

@Getter
public enum TagType {
    HIGH_QUALITY_PROVIDER(0, "高质量弹幕贡献者"),
    KNOWLEDGE_PROVIDER(1, "干货贡献者");
    private final Integer code;
    private final String tagName;

    TagType(Integer code, String tagName) {
        this.code = code;
        this.tagName = tagName;
    }
}

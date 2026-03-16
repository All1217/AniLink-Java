package com.video.web.main.vo;

import com.video.model.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "评论(Vo)")
public class CommentVo extends Comment {
    @Schema(description="用户昵称")
    private String nickname;

    @Schema(description="头像Url")
    private String avatar;

    @Schema(description="回复数量")
    private Integer replyCount;
}

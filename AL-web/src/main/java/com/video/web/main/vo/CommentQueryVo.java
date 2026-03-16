package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "评论查询条件集")
public class CommentQueryVo {
    @Schema(description="评论主ID")
    private Long id;

    @Schema(description="排序方式")
    private String sortField;

    @Schema(description="视频ID")
    private Long vid;

    @Schema(description="发起者ID")
    private Long uid;

    @Schema(description="根评论ID")
    private Long rootId;

    @Schema(description="被回复用户ID")
    private Long toUserId;

    @Schema(description="被回复评论ID")
    private Long parentId;
}

package com.video.web.main.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "关注&粉丝查找")
public class FollowQueryVo {
    @Schema(description="关注者")
    private Long followerId;
    @Schema(description="被关注者")
    private Long followeeId;
    @Schema(description="每页大小")
    private Integer size;
    @Schema(description="当前页")
    private Integer current;
    @Schema(description="排序条件")
    private String sortField;
}

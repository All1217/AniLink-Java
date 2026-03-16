package com.video.remark.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "点赞记录表单实体")
@Data
public class LikeDTO {
    @Schema(description = "业务ID")
    @NotNull(message = "业务id不能为空")
    private Long bizId;

    @Schema(description = "业务类型，0为视频点赞，1为评论/回复点赞")
    @NotNull(message = "业务类型不能为空")
    private Byte bizType;

    @Schema(description = "是否点赞")
    @NotNull(message = "是否点赞不能为空")
    private Boolean liked;
}

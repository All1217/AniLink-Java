package com.video.web.main.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.video.model.entity.BrowseHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "历史记录信息")
public class HistoryVo extends BrowseHistory {
    @Schema(description="作者UID")
    private Long authorUid;

    @Schema(description="作者昵称")
    private String nickname;

    @Schema(description="标题")
    private String title;

    @Schema(description="视频时长")
    private Double videoDuration;

    @Schema(description="封面")
    private String coverUrl;
}

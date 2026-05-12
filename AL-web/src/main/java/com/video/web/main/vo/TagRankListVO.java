package com.video.web.main.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "出现频率排行前八的标签VO")
public class TagRankListVO {
    @Schema(description = "标签名")
    private String name;

    @Schema(description = "出现次数")
    private Integer value;
}

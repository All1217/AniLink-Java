package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseEntity {
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
}

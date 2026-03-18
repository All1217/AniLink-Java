package com.video.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "热搜")
@Data
public class HotSearch implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String keyword;
    private String showName;
    private String icon;
    private String uri;
//    private String goto;
    private int heatScore;

}

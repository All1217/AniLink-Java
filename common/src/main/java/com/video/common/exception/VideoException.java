package com.video.common.exception;

import com.video.common.result.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VideoException extends RuntimeException{
    private Integer code;

    public VideoException(Integer code, String message){
        super(message);
        this.code = code;
    }

    public VideoException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}

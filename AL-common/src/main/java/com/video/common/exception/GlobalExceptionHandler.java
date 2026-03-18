package com.video.common.exception;

import com.video.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(VideoException.class)
    @ResponseBody
    public Result error(VideoException e){
        e.printStackTrace();
        return Result.fail(e.getCode(), e.getMessage());
    }
}

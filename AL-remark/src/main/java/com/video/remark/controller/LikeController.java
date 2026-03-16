package com.video.remark.controller;

import com.video.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/remark")
public class LikeController {
    @GetMapping("testGateway")
    public Result<String> testGateway() {
        return Result.ok("这是点赞微服务");
    }
}

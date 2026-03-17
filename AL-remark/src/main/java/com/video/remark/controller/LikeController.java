package com.video.remark.controller;

import com.video.common.result.Result;
import com.video.remark.domain.LikeDTO;
import com.video.remark.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "推荐微服务")
@RestController
@RequestMapping("/remark")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/like")
    @Operation(summary = "点赞")
    public void doLike(@Valid @RequestBody LikeDTO likeDTO) {
        likeService.doLike(likeDTO);
    }
}

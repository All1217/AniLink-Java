package com.video.web.main.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.video.common.result.Result;
import com.video.model.entity.Danmu;
import com.video.model.entity.UserInfo;
import com.video.web.main.service.DanmuService;
import com.video.web.main.service.UserInfoService;
import com.video.web.main.service.UserTagService;
import com.video.web.main.vo.RecQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static com.video.common.result.ResultCodeEnum.APP_SERVER_ERROR;

@Tag(name = "弹幕相关接口")
@RestController
@RequestMapping("/main/danmu")
public class DanmuContorller {
    @Autowired
    private DanmuService danmuService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserTagService userTagService;

    @Operation(summary = "发送弹幕")
    @PostMapping("/post")
    public Result postDanmu(@RequestBody Danmu danmu) {
        danmuService.insertOne(danmu);
        return Result.ok();
    }

    @Operation(summary = "查询弹幕列表")
    @GetMapping("/public/getDanmuList")
    public Result<List<Danmu>> getDanmuList(@RequestParam Long vid) {
        LambdaQueryWrapper<Danmu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Danmu::getVid, vid);
        queryWrapper.eq(Danmu::getStatus, 1);
        List<Danmu> list = danmuService.list(queryWrapper);
        return Result.ok(list);
    }

    @Operation(summary = "根据弹幕获取相似用户")
    @GetMapping("/getSimilarUsers")
    public Result<List<UserInfo>> getSimilarUsers(RecQueryVo queryVo) {
        // 向 Flask 服务发送 GET 请求，返回的视频 ID 列表
        String url = "http://localhost:5000/findSimilar/" + queryVo.getVid() + "/" + queryVo.getUid();
        List<Long> uids = restTemplate.getForObject(url, List.class);
        if (uids == null || uids.isEmpty()) {
            return Result.fail(APP_SERVER_ERROR.getCode(), "暂无数据！");
        }
        return userInfoService.getSimilarUsers(uids);
    }

    @Operation(summary = "根据指定标签过滤弹幕")
    @PostMapping("/filterUserByTags")
    public Result<List<Danmu>> filterUserByTags(@RequestBody RecQueryVo queryVo) {
        return danmuService.filterUserByTags(queryVo);
    }

    @Operation(summary = "根据指定标签过滤弹幕")
    @GetMapping("/getCommonTags")
    public Result<List<String>> getCommonTags(RecQueryVo queryVo) {
        return userTagService.getCommonTags(queryVo);
    }
}

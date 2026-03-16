package com.video.web.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.result.Result;
import com.video.model.entity.UserVideo;
import com.video.model.entity.Video;
import com.video.model.entity.VideoStats;
import com.video.web.main.service.VideoService;
import com.video.web.main.vo.RecQueryVo;
import com.video.web.main.vo.UserVideoQueryVo;
import com.video.web.main.vo.VideoQueryVo;
import com.video.web.main.vo.VideoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "视频与视频数据相关接口")
@RestController
@RequestMapping("/main/video")
public class VideoController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private VideoService videoService;

    @Operation(summary = "条件分页查询视频")
    @GetMapping("pageVideo")
    public Result<IPage<VideoVo>> videoPageItem(@RequestParam long current, @RequestParam long size, VideoQueryVo queryVo) {
        IPage<VideoVo> page = new Page<>(current, size);
        IPage<VideoVo> result = videoService.pageItem(page, queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "分页查询视频(免登录)")
    @GetMapping("/home/pageVideo")
    public Result<IPage<VideoVo>> homePageItem(@RequestParam long current, @RequestParam long size, VideoQueryVo queryVo) {
        log.info("queryVo: {}", queryVo);
        IPage<VideoVo> page = new Page<>(current, size);
        IPage<VideoVo> result = videoService.pageItem(page, queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "根据ID查询视频信息（免登录）")
    @GetMapping("/home/getVideoById")
    public Result<Video> homeGetVideoById(@RequestParam Long vid) {
        Video res = videoService.getById(vid);
        return Result.ok(res);
    }

    @Operation(summary = "根据ID查询视频数据（免登录）")
    @GetMapping("/home/getVideoStatsById")
    public Result<VideoStats> homeGetVideoStatsById(@RequestParam Long vid) {
        VideoStats res = videoService.getVideoStatsById(vid);
        return Result.ok(res);
    }

    /**
     * @description: 点赞相关
     */
    @Operation(summary = "点赞")
    @PostMapping("/interact/like")
    public Result<UserVideo> interActLike(@RequestBody UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = videoService.interActLike(userVideoQueryVo);
        return Result.ok(res);
    }

    @Operation(summary = "查询点赞状态")
    @GetMapping("/getInterActLike")
    public Result<UserVideo> getInterActLike(UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = videoService.getInterActLike(userVideoQueryVo);
        return Result.ok(res);
    }

    /**
     * @description: 投币相关
     */
    @Operation(summary = "查询投币状态")
    @GetMapping("/getInterActionCoin")
    public Result<UserVideo> getInterActionCoin(UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = videoService.getInterActionCoin(userVideoQueryVo);
        return Result.ok(res);
    }

    @Operation(summary = "投币")
    @PostMapping("/interact/coin")
    public Result<UserVideo> interActCoin(@RequestBody UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = videoService.interActCoin(userVideoQueryVo);
        return Result.ok(res);
    }

    @Operation(summary = "获取实时推荐列表")
    @GetMapping("/getRealTimeRecommend")
    public Result<List<VideoVo>> getRealTimeRecommend(RecQueryVo queryVo) {
        // 向 Flask 服务发送 GET 请求，返回的视频 ID 列表
        String url = "http://localhost:5000/recommendations/" + queryVo.getUid() + "?count=" + queryVo.getCount();
        List<Long> videoIds = restTemplate.getForObject(url, List.class);
        log.info("videoIds: {}", videoIds);
        List<VideoVo> res = new ArrayList<>();
        if (videoIds != null && !videoIds.isEmpty()) {
            res = videoService.getRealTimeRecommend(videoIds);
            log.info("res: {}", res);
        }
        return Result.ok(res);
    }

    /**
     * @description: 播放相关
     */
    @Operation(summary = "播放")
    @PostMapping("/public/play")
    public Result playVideo(@RequestBody UserVideoQueryVo userVideoQueryVo) {
        videoService.playVideo(userVideoQueryVo);
        return Result.ok();
    }

    /**
     * @description: 已弃，现改为分离接口，且走redis缓存
     */
    @Operation(summary = "查询交互数据")
    @GetMapping("/getInterActionStats")
    public Result<UserVideo> getInterActionStats(UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = videoService.getInterActionStats(userVideoQueryVo);
        return Result.ok(res);
    }

    /**
     * @description: 已弃，现改为分离接口，且走redis缓存
     */
//    @Operation(summary = "交互（除了播放）")
//    @PostMapping("/interact")
//    public Result recommend(@RequestBody UserVideoQueryVo userVideoQueryVo) {
//        videoService.recommend(userVideoQueryVo);
//        return Result.ok();
//    }

    /**
     * @description: 已弃，现改为分离接口，且走redis缓存
     */
    @Operation(summary = "播放")
    @PostMapping("/public/interact")
    public Result addPlay(@RequestBody UserVideoQueryVo userVideoQueryVo) {
        videoService.recommend(userVideoQueryVo);
        return Result.ok();
    }
}

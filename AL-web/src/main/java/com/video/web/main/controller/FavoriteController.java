package com.video.web.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.result.Result;
import com.video.model.entity.Favorite;
import com.video.model.entity.UserVideo;
import com.video.web.main.service.FavoriteService;
import com.video.web.main.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "收藏相关接口")
@RestController
@RequestMapping("/main/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Operation(summary = "新增")
    @PostMapping("/add")
    public Result insetOne(@RequestBody Favorite favorite) {
        //收藏夹数量上限在前端过滤
        favoriteService.insertOne(favorite);
        return Result.ok();
    }

    @Operation(summary = "查询收藏状态")
    @GetMapping("/getInterActionFavorite")
    public Result<UserVideo> getInterActionFavorite(UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = favoriteService.getInterActionFavorite(userVideoQueryVo);
        return Result.ok(res);
    }

    @Operation(summary = "分页获取收藏夹列表")
    @GetMapping("/getFavoriteListPage")
    public Result<IPage<Favorite>> getFavoriteListPage(@RequestParam long current,
                                                       @RequestParam long size,
                                                       @RequestParam long uid) {
        IPage<Favorite> page = new Page<>(current, size);
        IPage<Favorite> res = favoriteService.getFavoriteListPage(page, uid);
        return Result.ok(res);
    }

    @Operation(summary = "获取收藏列表")
    @GetMapping("/getVideoByFavor")
    public Result<List<VideoVo>> getVideoByFavor(@RequestParam long fid) {
        List<VideoVo> res = favoriteService.getVideoByFavor(fid);
        return Result.ok(res);
    }

    @Operation(summary = "分页获取收藏视频列表")
    @GetMapping("/getVideoFavoritePage")
    public Result<IPage<VideoFavoriteVo>> getVideoFavoritePage(@RequestParam long current,
                                                               @RequestParam long size,
                                                               @RequestParam long fid,
                                                               @RequestParam String sortField) {
        IPage<Favorite> page = new Page<>(current, size);
        IPage<VideoFavoriteVo> res = favoriteService.getVideoFavoritePage(page, fid, sortField);
        return Result.ok(res);
    }

    @Operation(summary = "修改收藏夹")
    @PutMapping("/update")
    public Result update(@RequestBody Favorite favorite) {
        favoriteService.update(favorite);
        return Result.ok();
    }

    @Operation(summary = "批量移动到收藏夹")
    @PutMapping("/update/batchMove")
    public Result<Integer> batchMove(@RequestBody BatchUpdateVo batchUpdateVo) {
        Integer res= favoriteService.batchMove(batchUpdateVo);
        return Result.ok(res);
    }

    @Operation(summary = "批量复制到收藏夹")
    @PutMapping("/update/batchCopy")
    public Result<Integer> batchCopy(@RequestBody BatchUpdateVo batchUpdateVo) {
        Integer res = favoriteService.batchCopy(batchUpdateVo);
        return Result.ok(res);
    }

    @Operation(summary = "将一个视频收藏到一个或多个收藏夹")
    @PutMapping("/collect")
    public Result collect(@RequestBody BatchCollectVo batchCollectVo) {
        favoriteService.collect(batchCollectVo);
        return Result.ok();
    }

    @Operation(summary = "将一个或多个视频取消收藏")
    @PutMapping("/unCollect")
    public Result unCollect(@RequestBody BatchUpdateVo batchUpdateVo) {
        favoriteService.unCollect(batchUpdateVo);
        return Result.ok();
    }

    @Operation(summary = "视频存在于哪些收藏夹")
    @GetMapping("/getFVByUidAndVid")
    public Result<List<Long>> getFVByUidAndVid(@RequestParam long uid, @RequestParam long vid) {
        List<Long> res = favoriteService.getFVByUidAndVid(uid, vid);
        return Result.ok(res);
    }
}

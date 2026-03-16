package com.video.web.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.result.Result;
import com.video.model.entity.Follow;
import com.video.model.entity.UserInfo;
import com.video.web.main.service.FansService;
import com.video.web.main.vo.FollowQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "粉丝管理相关接口")
@RestController
@RequestMapping("/main/fans")
public class FansController {
    @Autowired
    private FansService fansService;

    @Operation(summary = "关注")
    @PostMapping("/follow")
    public Result follow(@RequestBody Follow follow) {
        fansService.insertOne(follow);
        return Result.ok();
    }

    @Operation(summary = "查询关注状态")
    @GetMapping("/get/follow")
    public Result<Follow> getFollow(Follow follow) {
        Follow res = fansService.getFollow(follow);
        return Result.ok(res);
    }

    @Operation(summary = "取消关注")
    @DeleteMapping("/unfollow")
    public Result unfollow(@RequestBody Follow follow) {
        fansService.deleteById(follow);
        return Result.ok();
    }

    @Operation(summary = "查询粉丝列表")
    @GetMapping("/get/fans")
    public Result<Follow> follow(@RequestParam Long uid) {
        Follow res = new Follow();
        return Result.ok(res);
    }

    @Operation(summary = "查询关注列表")
    @GetMapping("/get/followList")
    public Result<IPage<UserInfo>> getFollowList(FollowQueryVo query) {
        IPage<UserInfo> page = new Page<>(query.getCurrent(), query.getSize());
        IPage<UserInfo> res = fansService.getFollowList(page, query);
        return Result.ok(res);
    }

    @Operation(summary = "查询粉丝列表")
    @GetMapping("/get/fansList")
    public Result<IPage<UserInfo>> getFansList(FollowQueryVo query) {
        IPage<UserInfo> page = new Page<>(query.getCurrent(), query.getSize());
        IPage<UserInfo> res = fansService.getFansList(page, query);
        return Result.ok(res);
    }
}

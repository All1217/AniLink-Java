package com.video.web.main.controller;

import com.video.common.result.Result;
import com.video.model.entity.UserInfo;
import com.video.model.entity.UserStats;
import com.video.web.main.service.UserInfoService;
import com.video.web.main.vo.RegisterVo;
import com.video.web.main.vo.UserInfoQueryVo;
import com.video.web.main.vo.UserStatsQueryVo;
import com.video.web.main.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户相关接口")
@RestController
@RequestMapping("/main/user")
@Slf4j
public class UserController {
    @Autowired
    private UserInfoService uiService;

    @Operation(summary = "按UID获取用户信息")
    @GetMapping("getById")
    public Result<UserInfo> getUserInfoById(@RequestParam Long uid) {
        UserInfo userInfo = uiService.getById(uid);
        return Result.ok(userInfo);
    }

    @Operation(summary = "按UID获取用户信息(免登录)")
    @GetMapping("/public/getById")
    public Result<UserInfo> getUserInfoByIdUnLogin(@RequestParam Long id) {
        UserInfo userInfo = uiService.getById(id);
        return Result.ok(userInfo);
    }

    @Operation(summary = "按UID获取用户额外信息(免登录)")
    @GetMapping("/public/getUserStatById")
    public Result<UserStats> getUserStatByIdUnLogin(@RequestParam Long id) {
        UserStats res = uiService.getUserStatsByUid(id);
        return Result.ok(res);
    }

    @Operation(summary = "按UID获取用户额外信息")
    @GetMapping("/getUserStatsByUid")
    public Result<UserStats> getUserStatsByUid(@RequestParam Long uid) {
        UserStats res = uiService.getUserStatsByUid(uid);
        return Result.ok(res);
    }

    @Operation(summary = "注册（新增用户）")
    @PostMapping("register")
    public Result<String> saveOrUpdate(@RequestBody RegisterVo registerVo) {
        String username = uiService.insertOne(registerVo);
        return Result.ok(username);
    }

    @Operation(summary = "修改公告")
    @PostMapping("/updateNotice")
    public Result updateNotice(@RequestBody UserStatsQueryVo queryVo) {
        uiService.updateNotice(queryVo);
        return Result.ok();
    }

    @Operation(summary = "修改个性签名")
    @PostMapping("/updateDescription")
    public Result updateDescription(@RequestBody UserInfoQueryVo queryVo) {
        uiService.updateDescription(queryVo);
        return Result.ok();
    }

    @Operation(summary = "修改头像")
    @PostMapping("/updateAvatar")
    public Result updateAvatar(@RequestBody UserInfoQueryVo queryVo) {
        uiService.updateAvatar(queryVo);
        return Result.ok();
    }

    @Operation(summary = "更新数据")
    @GetMapping("/updateMultiStats")
    public Result updateMultiStats(@RequestParam Long uid) {
        uiService.updateUserStats(uid);
        return Result.ok();
    }

    @Operation(summary = "更新用户信息")
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody UserInfoQueryVo queryVo) {
        log.info("queryVo: {}", queryVo);
        uiService.updateUserInfo(queryVo);
        return Result.ok();
    }

    /**
     * @description: 和前面按ID获取userInfo和userStats不同，这次获取的不是单独的userInfo或userStats，而是两个信息混合，形成一个Vo
     */
    @Operation(summary = "根据uid获取用户信息(Vo)")
    @GetMapping("/getUserVoByUid")
    public Result<UserVo> getUserVoByUid(UserInfoQueryVo queryVo) {
        UserVo res = uiService.getUserVoByUid(queryVo);
        return Result.ok(res);
    }

    @Operation(summary = "测试网关")
    @GetMapping("/public/gateway")
    public Result<String> gateway() {
        return Result.ok("这是Web服务");
    }
}

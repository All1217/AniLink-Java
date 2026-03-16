package com.video.web.main.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.result.Result;
import com.video.model.entity.BrowseHistory;
import com.video.model.entity.Favorite;
import com.video.web.main.service.HistoryService;
import com.video.web.main.vo.HistoryQueryVo;
import com.video.web.main.vo.HistoryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "历史记录相关接口")
@RestController
@RequestMapping("/main/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @Operation(summary = "新增历史记录")
    @PostMapping("/add")
    public Result insetOne(@RequestBody BrowseHistory browseHistory) {
        historyService.insertOne(browseHistory);
        return Result.ok();
    }

    @Operation(summary = "分页查找历史记录")
    @GetMapping("/getHistory/page")
    public Result<IPage<HistoryVo>> getHistoryPage(@RequestParam long current, @RequestParam long size, HistoryQueryVo queryVo) {
        IPage<HistoryVo> page = new Page<>(current, size);
        IPage<HistoryVo> result = historyService.getHistoryPage(page, queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "删除一条历史记录")
    @DeleteMapping("/deleteSingle")
    public Result deleteSingle(@RequestBody HistoryQueryVo queryVo) {
        historyService.deleteSingle(queryVo);
        return Result.ok();
    }

    @Operation(summary = "删除多条历史记录")
    @DeleteMapping("/deleteByVids")
    public Result deleteByVids(@RequestBody HistoryQueryVo queryVo) {
        historyService.deleteByVids(queryVo);
        return Result.ok();
    }

    @Operation(summary = "清空历史记录")
    @DeleteMapping("/deleteByUid")
    public Result deleteByUid(@RequestBody HistoryQueryVo queryVo) {
        historyService.deleteByUid(queryVo);
        return Result.ok();
    }
}

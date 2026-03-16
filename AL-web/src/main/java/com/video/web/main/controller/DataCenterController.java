package com.video.web.main.controller;

import com.video.common.result.Result;
import com.video.model.entity.HotSearch;
import com.video.web.main.service.DataCenterService;
import com.video.web.main.vo.DataResVo;
import com.video.web.main.vo.DataTotal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "数据分析相关接口")
@RestController
@RequestMapping("/main/dataCenter")
public class DataCenterController {
    @Autowired
    private DataCenterService dataCenterService;

    @Operation(summary = "查询总播放量")
    @GetMapping("/total/get/play")
    public Result<DataResVo> getTotalPlay(@RequestParam Long uid,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        DataResVo res = dataCenterService.getTotalPlay(uid, begin, end);
        return Result.ok(res);
    }
    @Operation(summary = "查询总点赞数")
    @GetMapping("/total/get/like")
    public Result<DataResVo> getLike(@RequestParam Long uid,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        DataResVo res = dataCenterService.getLike(uid, begin, end);
        return Result.ok(res);
    }

    @Operation(summary = "获取热搜列表")
    @GetMapping("/mainWindow/get/hotSearch")
    public Result<List<HotSearch>> getHotSearch() {
        List<HotSearch> res = dataCenterService.getHotSearch();
        return Result.ok(res);
    }
}

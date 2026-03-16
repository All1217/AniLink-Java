package com.video.web.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.BrowseHistory;
import com.video.web.main.vo.HistoryQueryVo;
import com.video.web.main.vo.HistoryVo;

public interface HistoryService {
    void insertOne(BrowseHistory browseHistory);

    IPage<HistoryVo> getHistoryPage(IPage<HistoryVo> page, HistoryQueryVo queryVo);

    void deleteByVids(HistoryQueryVo queryVo);

    void deleteSingle(HistoryQueryVo queryVo);

    void deleteByUid(HistoryQueryVo queryVo);
}

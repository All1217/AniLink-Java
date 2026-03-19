package com.video.web.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.video.model.dto.web.VideoRecordFormDTO;
import com.video.model.entity.BrowseHistory;
import com.video.web.main.vo.HistoryQueryVo;
import com.video.web.main.vo.HistoryVo;

public interface HistoryService extends IService<BrowseHistory> {
    void insertOne(BrowseHistory browseHistory);

    IPage<HistoryVo> getHistoryPage(IPage<HistoryVo> page, HistoryQueryVo queryVo);

    void deleteByVids(HistoryQueryVo queryVo);

    void deleteSingle(HistoryQueryVo queryVo);

    void deleteByUid(HistoryQueryVo queryVo);

    boolean addVideoRecord(VideoRecordFormDTO formDTO);
}

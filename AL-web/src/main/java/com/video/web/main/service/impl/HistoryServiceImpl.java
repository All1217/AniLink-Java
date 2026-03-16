package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.BrowseHistory;
import com.video.web.main.mapper.HistoryMapper;
import com.video.web.main.service.HistoryService;
import com.video.web.main.vo.HistoryQueryVo;
import com.video.web.main.vo.HistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public void insertOne(BrowseHistory browseHistory) {
        historyMapper.insertOne(browseHistory);
    }

    @Override
    public IPage<HistoryVo> getHistoryPage(IPage<HistoryVo> page, HistoryQueryVo queryVo) {
        return historyMapper.getHistoryPage(page, queryVo);
    }

    @Override
    public void deleteByVids(HistoryQueryVo queryVo) {
        queryVo.getVids().forEach(vid -> {
            historyMapper.delete(queryVo.getUid(), vid);
        });
    }

    @Override
    public void deleteSingle(HistoryQueryVo queryVo) {
        historyMapper.delete(queryVo.getUid(), queryVo.getVids().get(0));
    }

    @Override
    public void deleteByUid(HistoryQueryVo queryVo) {
        historyMapper.deleteByUid(queryVo.getUid());
    }
}

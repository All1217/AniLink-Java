package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.common.login.LoginUser;
import com.video.common.login.LoginUserHolder;
import com.video.model.dto.web.VideoRecordFormDTO;
import com.video.model.entity.BrowseHistory;
import com.video.web.main.mapper.HistoryMapper;
import com.video.web.main.service.HistoryService;
import com.video.web.main.task.VideoRecordDelayTaskHandler;
import com.video.web.main.vo.HistoryQueryVo;
import com.video.web.main.vo.HistoryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, BrowseHistory> implements HistoryService {
    private final HistoryMapper historyMapper;
    private final VideoRecordDelayTaskHandler taskHandler;

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


    @Override
    public boolean addVideoRecord(VideoRecordFormDTO formDTO) {
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        if (loginUser == null) {
            return false;
        }
        Long uid = loginUser.getUserId();
        // 1.查询旧的观看记录
        BrowseHistory old = queryOldRecord(formDTO.getVid(), uid);
        // 2.判断是否存在
        if (old == null) {
            // 3.不存在,则新增
            // 3.1.转换PO
            BrowseHistory record = new BrowseHistory();
            record.setVid(formDTO.getVid());
            record.setUid(uid);
            record.setViewTime(Timestamp.valueOf(formDTO.getCommitTime()));
            record.setDuration(formDTO.getMoment());
            // 3.写入数据库
            historyMapper.insert(record);
            return false;
        }
        // 4.存在,则更新
        BrowseHistory record = new BrowseHistory();
        record.setVid(formDTO.getVid());
        record.setUid(uid);
        record.setDuration(formDTO.getMoment());
        record.setId(old.getId());
        // TODO: 目前业务无法统计某个视频累计播放时长，也就无法统计完播率
        taskHandler.addVideoRecordTask(record);
        return true;
    }

    private BrowseHistory queryOldRecord(Long vid, Long uid) {
        //1.查询缓存
        BrowseHistory record = taskHandler.readRecordCache(vid, uid);
        // 2.如果命中,直接返回
        if (record != null) {
            return record;
        }
        // 3.未命中,查询数据库
        record = lambdaQuery()
                .eq(BrowseHistory::getVid, vid)
                .eq(BrowseHistory::getUid, uid)
                .one();
        // 4.写入缓存
        taskHandler.writeRecordCache(record);
        return record;
    }
}

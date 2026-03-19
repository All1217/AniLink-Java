package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.BrowseHistory;
import com.video.web.main.vo.HistoryQueryVo;
import com.video.web.main.vo.HistoryVo;
import org.apache.ibatis.annotations.Delete;

public interface HistoryMapper extends BaseMapper<BrowseHistory> {
    void insertOne(BrowseHistory browseHistory);

    IPage<HistoryVo> getHistoryPage(IPage<HistoryVo> page, HistoryQueryVo queryVo);

    void delete(Long uid, Long vid);

    @Delete("delete from bh_video where uid = #{uid}")
    void deleteByUid(Long uid);
}

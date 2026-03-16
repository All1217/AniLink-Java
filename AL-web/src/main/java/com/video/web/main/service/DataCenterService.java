package com.video.web.main.service;

import com.video.model.entity.HotSearch;
import com.video.web.main.vo.DataResVo;
import com.video.web.main.vo.DataTotal;

import java.time.LocalDate;
import java.util.List;

public interface DataCenterService {
    DataResVo getTotalPlay(Long uid, LocalDate begin, LocalDate end);

    DataResVo getLike(Long uid, LocalDate begin, LocalDate end);

    List<HotSearch> getHotSearch();
}

package com.video.web.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.video.common.result.Result;
import com.video.model.entity.Danmu;
import com.video.web.main.vo.RecQueryVo;

import java.util.List;

public interface DanmuService extends IService<Danmu> {
    void insertOne(Danmu danmu);

    Result<List<Danmu>> filterUserByTags(RecQueryVo queryVo);
}

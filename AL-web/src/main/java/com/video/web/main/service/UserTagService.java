package com.video.web.main.service;

import com.video.common.result.Result;
import com.video.web.main.vo.RecQueryVo;

import java.util.List;

public interface UserTagService {

    Result<List<String>> getCommonTags(RecQueryVo queryVo);
}

package com.video.web.main.service;

import com.video.common.result.Result;
import com.video.web.main.vo.RecQueryVo;
import com.video.web.main.vo.TagRankListVO;

import java.util.List;

public interface UserTagService {

    Result<List<String>> getCommonTags(RecQueryVo queryVo);

    Result<List<TagRankListVO>> getTagRankList(Long vid);
}

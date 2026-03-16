package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.common.result.Result;
import com.video.model.entity.Danmu;
import com.video.model.enums.TagType;
import com.video.web.main.mapper.DanmuMapper;
import com.video.web.main.service.DanmuService;
import com.video.web.main.vo.RecQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.video.common.result.ResultCodeEnum.PARAM_ERROR;

@Slf4j
@Service
public class DanmuServiceImpl extends ServiceImpl<DanmuMapper, Danmu> implements DanmuService {

    @Autowired
    private DanmuMapper danmuMapper;

    @Override
    public void insertOne(Danmu danmu) {
        log.info("danmu: {}", danmu);
        danmu.setId(0L);
        danmuMapper.insert(danmu);
    }

    @Override
    public Result<List<Danmu>> filterUserByTags(RecQueryVo queryVo) {
        // 1. 参数校验
        if (queryVo == null) {
            return Result.fail(PARAM_ERROR.getCode(), "请求参数不能为空");
        }
        Integer tagCode = queryVo.getTag();
        if (tagCode == null) {
            return Result.fail(PARAM_ERROR.getCode(), "标签代码不能为空");
        }
        // 2. 标签代码转换
        String tagName = convertCodeToTagName(tagCode);
        if (!StringUtils.hasText(tagName)) {
            return Result.fail(PARAM_ERROR.getCode(), "无效的标签代码");
        }
        List<Danmu> res = danmuMapper.filterUserByTags(queryVo.getVid(), tagName);
        return Result.ok(res);
    }

    private String convertCodeToTagName(Integer code) {
        for (TagType tagType : TagType.values()) {
            if (tagType.getCode().equals(code)) {
                return tagType.getTagName();
            }
        }
        return null;
    }
}

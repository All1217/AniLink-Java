package com.video.web.main.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.common.constant.Constant;
import com.video.model.entity.HotSearch;
import com.video.web.main.mapper.DataCenterMapper;
import com.video.web.main.service.DataCenterService;
import com.video.web.main.vo.DataResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
public class DataCenterServiceImpl implements DataCenterService {
    @Autowired
    private DataCenterMapper dataCenterMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public DataResVo getTotalPlay(Long uid, LocalDate begin, LocalDate end) {
        if (begin.isAfter(end)) {
            // begin>end则交换两个日期
            LocalDate temp = begin;
            begin = end;
            end = temp;
        }
        LocalDate lEnd = begin.minusDays(1);
        LocalDate lBegin = lEnd;
        List<LocalDate> dateList1 = new ArrayList<>();
        List<LocalDate> dateList2 = new ArrayList<>();
        dateList1.add(begin);
        dateList2.add(lEnd);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            lBegin = lBegin.minusDays(1);
            dateList1.add(begin);
            dateList2.add(lBegin);
        }
        DataResVo res = new DataResVo();
        //本周期
        DataResVo dr1 = batchPlay(dateList1, uid);
        //上周期
        Collections.reverse(dateList2);
        DataResVo dr2 = batchPlay(dateList2, uid);
        res.setMainRes(dr1.getMainRes());
        res.setSubRes(dr1.getSubRes());
        res.setLastMainRes(dr2.getMainRes());
        res.setLastSubRes(dr2.getSubRes());
        return res;
    }

    public DataResVo batchPlay(List<LocalDate> dateList, Long uid) {
        List<Long> totalTurnoverList = new ArrayList<>();
        List<Long> fansTurnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("uid", uid);
            //总播放
            Long tTurnover = dataCenterMapper.sumPlayByMap(map);
            tTurnover = tTurnover == null ? 0 : tTurnover;
            totalTurnoverList.add(tTurnover);
            //粉丝播放
            Long f = dataCenterMapper.sumFansPlayByMap(map);
            f = f == null ? 0 : f;
            fansTurnoverList.add(f);
        }
        DataResVo res = new DataResVo();
        res.setMainRes(totalTurnoverList);
        res.setSubRes(fansTurnoverList);
        return res;
    }

    @Override
    public DataResVo getLike(Long uid, LocalDate begin, LocalDate end) {
        if (begin.isAfter(end)) {
            // begin>end则交换两个日期
            LocalDate temp = begin;
            begin = end;
            end = temp;
        }
        LocalDate lEnd = begin.minusDays(1);
        LocalDate lBegin = lEnd;
        List<LocalDate> dateList1 = new ArrayList<>();
        List<LocalDate> dateList2 = new ArrayList<>();
        dateList1.add(begin);
        dateList2.add(lEnd);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            lBegin = lBegin.minusDays(1);
            dateList1.add(begin);
            dateList2.add(lBegin);
        }
        DataResVo res = new DataResVo();
        //本周期
        DataResVo dr1 = batchLike(dateList1, uid);
        //上周期
        Collections.reverse(dateList2);
        DataResVo dr2 = batchLike(dateList2, uid);

        res.setMainRes(dr1.getMainRes());
        res.setLastMainRes(dr2.getMainRes());
        return res;
    }

    @Override
    public List<HotSearch> getHotSearch() {
        List<HotSearch> res = new ArrayList<>();
        Map<String, Object> json = (Map<String, Object>) redisTemplate.opsForValue().get(Constant.MAIN_HOT_SEARCH);
        try {
            if (json == null) {
                return res;
            }
            log.info("result: {}", (List<HotSearch>) json.get("list"));
            if (json != null) {
                res = (List<HotSearch>) json.get("list");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public DataResVo batchLike(List<LocalDate> dateList, Long uid) {
        List<Long> totalTurnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("uid", uid);
            Long tTurnover = dataCenterMapper.sumLikeByMap(map);
            tTurnover = tTurnover == null ? 0 : tTurnover;
            totalTurnoverList.add(tTurnover);
        }
        DataResVo res = new DataResVo();
        res.setMainRes(totalTurnoverList);
        return res;
    }
}

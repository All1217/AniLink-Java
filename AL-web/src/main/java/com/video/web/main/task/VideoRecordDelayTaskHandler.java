package com.video.web.main.task;

import com.video.common.utils.JsonUtils;
import com.video.common.utils.StringUtils;
import com.video.model.entity.BrowseHistory;
import com.video.web.main.mapper.HistoryMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;

import static com.video.common.constant.Constant.RECORD_KEY_TEMPLATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoRecordDelayTaskHandler {
    private final StringRedisTemplate redisTemplate;
    private final DelayQueue<DelayTask<RecordTaskData>> queue = new DelayQueue<>();
    private final HistoryMapper historyMapper;
    private static volatile boolean begin = true;

    @PostConstruct
    public void init() {
        CompletableFuture.runAsync(this::handleDelayTask);
    }

    @PreDestroy
    public void destroy() {
        begin = false;
        log.debug("延时任务停止执行！");
    }

    public void handleDelayTask() {
        while (begin) {
            try {
                // 1.获取到期的延迟任务
                DelayTask<RecordTaskData> task = queue.take();
                RecordTaskData data = task.getData();
                // 2.查询Redis缓存
                BrowseHistory record = readRecordCache(data.getVid(), data.getUid());
                if (record == null) {
                    continue;
                }
                // 3.比较数据,moment值
                if (!Objects.equals(data.getMoment(), record.getDuration())) {
                    // 不一致,说明用户还在持续提交播放进度,放弃旧数据
                    continue;
                }
                //4.一致,持久化播放进度数据到数据库
                // 4.1.更新视频播放的时间节点
                historyMapper.updateById(record);
            } catch (Exception e) {
                log.error("处理延迟任务发生异常", e);
            }
        }
    }

    public void addVideoRecordTask(BrowseHistory record) {
        // 1.添加数据到Redis缓存
        writeRecordCache(record);
        //2.提交延迟任务到延迟队列DelayQueue
        queue.add(new DelayTask<>(new RecordTaskData(record), Duration.ofSeconds(10)));
    }

    public void writeRecordCache(BrowseHistory record) {
        //写缓存
        log.debug("更新视频播放记录的缓存数据");
        try {
            // 1.数据转换
            String json = JsonUtils.toJsonStr(new RecordCacheData(record));
            // 2.写入Redis
            String key = StringUtils.format(RECORD_KEY_TEMPLATE, record.getVid());
            redisTemplate.opsForHash().put(key, record.getUid().toString(), json);
            // 3.添加缓存过期时间
            redisTemplate.expire(key, Duration.ofMinutes(1));
        } catch (Exception e) {
            log.error("更新视频播放记录缓存异常", e);
            e.printStackTrace();
        }
    }

    public BrowseHistory readRecordCache(Long vid, Long uid) {
        //读缓存
        try {
            //1.读取Redis数据
            String key = StringUtils.format(RECORD_KEY_TEMPLATE, vid);
            Object cacheData = redisTemplate.opsForHash().get(key, uid.toString());
            if (cacheData == null) {
                return null;
            }
            // 2.数据检查和转换
            RecordCacheData temp = JsonUtils.toBean(cacheData.toString(), RecordCacheData.class);
            BrowseHistory res = new BrowseHistory();
            res.setDuration(temp.getMoment());
            res.setId(temp.getId());
            return res;
        } catch (Exception e) {
            log.error("缓存读取异常", e);
            return null;
        }
    }

    public void cleanRecordCache(Long vid, Long uid) {
        // 删除缓存
        String key = StringUtils.format(RECORD_KEY_TEMPLATE, vid);
        redisTemplate.opsForHash().delete(key, uid.toString());
    }

    @Data
    @NoArgsConstructor
    private static class RecordCacheData {
        private Double moment;
        private Long id;

        public RecordCacheData(BrowseHistory record) {
            this.moment = record.getDuration();
            this.id = record.getId();
        }
    }

    @Data
    @NoArgsConstructor
    private static class RecordTaskData {
        private Long vid;
        private Long uid;
        private Double moment;

        public RecordTaskData(BrowseHistory record) {
            this.vid = record.getVid();
            this.uid = record.getUid();
            this.moment = record.getDuration();
        }
    }
}
package com.video.remark.service.impl;

import com.video.common.config.mq.RabbitMqHelper;
import com.video.common.login.LoginUser;
import com.video.common.login.LoginUserHolder;
import com.video.common.result.Result;
import com.video.common.utils.CollUtils;
import com.video.common.utils.StringUtils;
import com.video.remark.domain.LikeDTO;
import com.video.model.dto.remark.LikedTimesDTO;
import com.video.remark.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.video.common.constant.Constant.LIKES_BIZ_KEY_PREFIX;
import static com.video.common.constant.Constant.LIKES_TIMES_KEY_PREFIX;
import static com.video.common.constant.MqConstant.LIKED_TIMES_KEY_TEMPLATE;
import static com.video.common.constant.MqConstant.LIKE_RECORD_EXCHANGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {
    private final RabbitMqHelper mqHelper;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void doLike(LikeDTO likeDTO) {
        // 1.基于前端的参数,判断是执行点赞还是取消点赞
        boolean success = likeDTO.getLiked() ? like(likeDTO) : unlike(likeDTO);
        // 2.判断是否执行成功,如果失败,则直接结束。如果已经有了对应的用户ID，再次插入相同ID会失败，从而避免重复刷点赞
        if (!success) {
            return;
        }
        // 3.如果执行成功,统计点赞总数
        Long likedTimes = redisTemplate.opsForSet()
                .size(LIKES_BIZ_KEY_PREFIX + likeDTO.getBizId());
        if (likedTimes == null || likedTimes == 0) {
            return;
        }
        // 4.缓存点总数到Redis
        redisTemplate.opsForZSet().add(
                LIKES_TIMES_KEY_PREFIX + likeDTO.getBizType(),
                likeDTO.getBizId().toString(),
                likedTimes);
    }

    @Override
    public void readLikedTimesAndSendMessage(Byte bizType, int maxBizSize) {
        // 1.读取并移除Redis中缓存的点赞总数
        String key = LIKES_TIMES_KEY_PREFIX + bizType;
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().popMin(key, maxBizSize);//优先处理点赞数小的
        if (CollUtils.isEmpty(tuples)) {
            return;
        }
        // 2.数据转换
        List<LikedTimesDTO> list = new ArrayList<>(tuples.size());
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String bizId = tuple.getValue();
            Double likedTimes = tuple.getScore();
            if (bizId == null || likedTimes == null) {
                continue;
            }
            list.add(LikedTimesDTO.of(Long.valueOf(bizId), likedTimes.intValue()));
        }
//        log.info("获取到点赞总数：{}", list);
        // 3.发送MQ消息
        mqHelper.send(
                LIKE_RECORD_EXCHANGE,
                StringUtils.format(LIKED_TIMES_KEY_TEMPLATE, bizType),
                list);
    }

    @Override
    public Result<Integer> getLikeState(Long bizId) {
        Long userId = LoginUserHolder.getLoginUser().getUserId();
        String key = LIKES_BIZ_KEY_PREFIX + bizId;
        Boolean isMember = redisTemplate.opsForSet().isMember(key, userId.toString());
        Integer res = Boolean.TRUE.equals(isMember) ? 1 : 0;
        return Result.ok(res);
    }

    private boolean like(LikeDTO likeDTO) {
        // 1.获取用户id
        LoginUser u = LoginUserHolder.getLoginUser();
        Long uid = u.getUserId();
        // 2.获取Key
        String key = LIKES_BIZ_KEY_PREFIX + likeDTO.getBizId();
        //3.执行SADD命令
        Long result = redisTemplate.opsForSet().add(key, uid.toString());
        return result != null && result > 0;
    }

    private boolean unlike(LikeDTO likeDTO) {
        Long uid = LoginUserHolder.getLoginUser().getUserId();
        String key = LIKES_BIZ_KEY_PREFIX + likeDTO.getBizId();
        Long result = redisTemplate.opsForSet().remove(key, uid.toString());
        return result != null && result > 0;
    }
}

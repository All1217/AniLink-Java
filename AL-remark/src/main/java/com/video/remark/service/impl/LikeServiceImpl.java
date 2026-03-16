package com.video.remark.service.impl;

import com.video.common.login.LoginUserHolder;
import com.video.remark.domain.LikeDTO;
import com.video.remark.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.video.common.constant.Constant.LIKES_BIZ_KEY_PREFIX;
import static com.video.common.constant.Constant.LIKES_TIMES_KEY_PREFIX;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void doLike(LikeDTO likeDTO) {
        // 1.基于前端的参数,判断是执行点赞还是取消点赞
        boolean success = likeDTO.getLiked() ? like(likeDTO) : unlike(likeDTO);
        // 2.判断是否执行成功,如果失败,则直接结束
        if (!success) {
            return;
        }
        // 3.如果执行成功,统计点赞总数
        Long likedTimes = redisTemplate.opsForSet()
                .size(LIKES_BIZ_KEY_PREFIX + likeDTO.getBizId());
        if (likedTimes == null) {
            return;
        }
        // 4.缓存点总数到Redis
        redisTemplate.opsForZSet().add(
                LIKES_TIMES_KEY_PREFIX + likeDTO.getBizType(),
                likeDTO.getBizId().toString(),
                likedTimes);
    }

    private boolean like(LikeDTO likeDTO) {
        // 1.获取用户id
        Long uid = LoginUserHolder.getLoginUser().getUserId();
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

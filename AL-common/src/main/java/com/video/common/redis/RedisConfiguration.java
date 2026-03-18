package com.video.common.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, Object> stringObjectRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
//        //redis提供的string类型的序列化器
//        template.setKeySerializer(RedisSerializer.string());
//        //JDK提供的string类型的序列化器
//        template.setValueSerializer(RedisSerializer.java());

        // 使用 Jackson2JsonRedisSerializer 来处理 JSON 格式的数据
        Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 设置 key 和 value 的序列化器
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(jsonSerializer);
        return template;
    }
}

package com.video.common.config.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;

import java.time.Duration;

/**
 * 延迟消息处理器：在BasicIdMessageProcessor基础上，为消息添加延迟发送属性
 * 通过设置消息头的"x-delay"字段，实现RabbitMQ延迟消息功能（需配合延迟插件使用）
 */
public class DelayedMessageProcessor extends BasicIdMessageProcessor {

    private final long delay;

    public DelayedMessageProcessor(Duration delay) {
        this.delay = delay.toMillis();
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        // 1.添加消息id
        super.postProcessMessage(message);
        // 2.添加延迟时间
        message.getMessageProperties().setHeader("x-delay", delay);
        return message;
    }
}

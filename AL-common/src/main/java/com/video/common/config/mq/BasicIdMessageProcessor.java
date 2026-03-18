package com.video.common.config.mq;

import cn.hutool.core.lang.UUID;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import static com.video.common.constant.Constant.REQUEST_ID_HEADER;

/**
 * 消息预处理处理器：为消息添加请求链路ID（Request ID），有有点像MQ消息专属拦截器
 *
 * <p>核心作用：
 * 在消息发送前，将当前线程中的请求ID（来自MDC）写入消息头。
 * 这样消费者服务可以从消息头中获取同一个请求ID，实现分布式链路追踪。
 *
 * <p>工作流程：
 * <pre>
 * 生产者服务（发送消息）                消费者服务（接收消息）
 *       │                                      │
 *       │ 1. 业务处理时有请求ID                │
 *       │    MDC.put("requestId", "xxx")       │
 *       │                                      │
 *       │ 2. 发送消息                          │
 *       │    rabbitTemplate.convertAndSend()   │
 *       │          ↓                           │
 *       │    BasicIdMessageProcessor           │
 *       │    → 从MDC获取请求ID                  │
 *       │    → 写入消息头 "request-id"          │
 *       │                                      │
 *       │    【消息通过网络传输】                │
 *       │                                      │
 *       │                                      │ 3. 接收消息
 *       │                                      │    AfterReceivePostProcessor
 *       │                                      │    → 从消息头读取请求ID
 *       │                                      │    → 放入当前线程的MDC
 *       │                                      │
 *       │                                      │ 4. 业务处理
 *       │                                      │    log.info("处理请求：{}",
 *       │                                      │           MDC.get("requestId"))
 * </pre>
 *
 * <p>设计目的：
 * <ul>
 *   <li><b>链路追踪</b>：在微服务架构中，一个请求可能经过多个服务。
 *       通过传递同一个请求ID，可以将所有相关日志串联起来</li>
 *   <li><b>问题排查</b>：当出现异常时，可以根据请求ID快速定位整个调用链</li>
 *   <li><b>监控统计</b>：可以按请求ID统计服务处理时间、成功率等</li>
 * </ul>
 *
 * <p>使用方式：
 * <pre>
 * // 在发送消息时传入此processor
 * rabbitTemplate.convertAndSend(exchange, routingKey, message,
 *        new BasicIdMessageProcessor());
 *
 * // 消费者端需要配置对应的接收处理器，将请求ID从消息头恢复到MDC
 * factory.setAfterReceivePostProcessors(message -> {
 *     Object requestId = message.getMessageProperties().getHeader(REQUEST_ID_HEADER);
 *     if (requestId != null) {
 *         MDC.put(REQUEST_ID_HEADER, requestId.toString());
 *     }
 *     return message;
 * });
 * </pre>
 *
 * TODO: 该写法涉及设计模式的装饰器模式，后续需深入了解
 *
 * @see org.springframework.amqp.core.MessagePostProcessor
 * @see org.slf4j.MDC
 * @see com.video.common.constant.Constant#REQUEST_ID_HEADER
 */
public class BasicIdMessageProcessor implements MessagePostProcessor {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        // MDC（Mapped Diagnostic Context） 是 SLF4J 提供的线程上下文的诊断上下文
        // 它的核心作用是：在同一个请求的处理链路中，传递共同的上下文信息。
        // 这种设计有助于在当前线程通过一个ID串联不同业务
        String requestId = MDC.get(REQUEST_ID_HEADER);
        if (requestId == null) {
            // 降级措施，如果MDC失效用UUID
            requestId = UUID.randomUUID().toString(true);
        }
        // 写入RequestID标示
        message.getMessageProperties().setHeader(REQUEST_ID_HEADER, requestId);
        return message;
    }
}

package com.video.common.constant;

public class MqConstant {
    /*异常信息的交换机*/
    public final static String ERROR_EXCHANGE = "error.topic";
    /*异常RoutingKey的前缀*/
    public final static String ERROR_KEY_PREFIX = "error.";
    public final static String ERROR_QUEUE_TEMPLATE = "error.{}.queue";
    /*点赞记录有关的交换机*/
    public final static String LIKE_RECORD_EXCHANGE = "like.record.topic";
    /*点赞的RoutingKey*/
    public final static String LIKED_TIMES_KEY_TEMPLATE = "{}.times.changed";
    /*问答*/
    public final static String VIDEO_LIKED_TIMES_KEY = "0.times.changed";
}


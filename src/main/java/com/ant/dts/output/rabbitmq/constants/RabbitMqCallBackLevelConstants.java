package com.ant.dts.output.rabbitmq.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RabbitMq 回调静态文件
 * </p>
 *
 * @author Ant
 * @since 2022-04-11 18:37
 */
public class RabbitMqCallBackLevelConstants {

    /**
     * rabbitMQ 消息发送失败处理等级配置
     */
    public static final Map<String, RabbitMqLevel> RABBITMQ_CALL_BACK_LEVEL = new HashMap<String, RabbitMqLevel>(){{

    }};


    public enum RabbitMqLevel {
        LEVEL1, LEVEL2, LEVEL3
    }
}

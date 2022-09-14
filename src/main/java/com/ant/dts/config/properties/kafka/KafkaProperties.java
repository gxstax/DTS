package com.ant.dts.config.properties.kafka;

import lombok.Data;

/**
 * <p>
 * Kafka 配置信息
 * </p>
 *
 * @author Ant
 * @since 2022/1/4 10:53 上午
 */
@Data
public class KafkaProperties {
    /**
     * kafka 客户端共有参数
     */
    private CommonClientProperties common;

    /**
     * kafka 管理端配置参数
     */
    private AdminProperties admin;

    /**
     * kafka 消费者端配置参数
     */
    private ConsumerProperties consumer;

    /**
     * kafka 生产端配置参数
     */
    private ProducerProperties producer;
}

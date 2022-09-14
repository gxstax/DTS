package com.ant.dts.output.rabbitmq.producer.sender;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * <p>
 * rabbitmq消息发送服务类
 * </p>
 *
 * @author Ant
 * @since 2022/4/11 9:00 下午
 */
public interface RabbitMqSender {

    /**
     * <p>
     * 发送到默认交换机
     * </p>
     *
     * @param message
     * @return void
     */
    void send(Object message);

    /**
     * <p>
     * 按指定路由键发送
     * </p>
     *
     * @param routeKey
     * @param message
     * @return void
     */
    void send(String routeKey, Object message);

    /**
     * <p>
     * 指定交换机和路由键
     * </p>
     *
     * @param exchangeName
     * @param routeKey
     * @param message
     * @return void
     */
    void send(String exchangeName, String routeKey, Object message);

    /**
     * <p>
     * 带有业务唯一识别信息
     * </p>
     *
     * @param exchangeName
     * @param routeKey
     * @param message
     * @param correlationData 一般是业务唯一识别，用作幂等或异常查找
     * @return
     */
    void send(String exchangeName, String routeKey, Object message, CorrelationData correlationData);

    /**
     * <p>
     * 自定义消息确认回调函数
     * </p>
     *
     * @param exchangeName
     * @param routeKey
     * @param message
     * @param correlationData
     * @param callback
     * @return void
     */
    void send(String exchangeName, String routeKey, Object message, CorrelationData correlationData, RabbitTemplate.ConfirmCallback callback);
}

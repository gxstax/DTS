package com.ant.dts.output.rabbitmq.producer.sender;

import com.ant.dts.output.rabbitmq.producer.confirm.MsgSendConfirmCallBack;
import com.ant.dts.output.rabbitmq.producer.returns.MessageReturnCallBack;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <p>
 * rabbitmq 消息发送服务类
 * </p>
 *
 * @author Ant
 * @since 2022/4/11 9:01 下午
 */
@Component
public class RabbitMqMessageSender implements RabbitMqSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        // 设置失败回调对象
        rabbitTemplate.setReturnCallback(new MessageReturnCallBack());
        // 设置消息确认对象
        rabbitTemplate.setConfirmCallback(new MsgSendConfirmCallBack());
    }

    /**
     * <p>
     * 发送到默认交换机
     * </p>
     *
     * @param message
     * @return void
     */
    @Override
    public void send(Object message) {
        rabbitTemplate.convertAndSend(message);
    }

    /**
     * <p>
     * 按指定路由键发送
     * </p>
     *
     * @param routeKey
     * @param message
     * @return void
     */
    @Override
    public void send(String routeKey, Object message) {
        rabbitTemplate.convertAndSend(routeKey, message);
    }

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
    @Override
    public void send(String exchangeName, String routeKey, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, routeKey, message);
    }

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
    @Override
    public void send(String exchangeName, String routeKey, Object message, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(exchangeName, routeKey, message, correlationData);
    }

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
    @Override
    public void send(String exchangeName, String routeKey, Object message, CorrelationData correlationData, RabbitTemplate.ConfirmCallback callback) {
        rabbitTemplate.setConfirmCallback(callback);
        rabbitTemplate.convertAndSend(exchangeName, routeKey, message, correlationData);
    }
}

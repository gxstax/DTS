package com.ant.dts.output.rabbitmq.producer.returns;

import com.ant.dts.output.rabbitmq.constants.RabbitMqCallBackLevelConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * <p>
 * 消息发送失败回调
 * </p>
 *
 * @author Ant
 * @since 2022-04-11 18:37
 */
@Slf4j
public class MessageReturnCallBack implements RabbitTemplate.ReturnCallback {

    /**
     * <p>
     * 功能描述
     * </p>
     *
     * @param message 发送消息 + 消息配置信息
     * @param replyCode 状态码
     * @param replyText 失败信息
     * @param exchange 交换机名称
     * @param routingKey 路由键名称
     * @return void
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        // 一级落数据库
        if (RabbitMqCallBackLevelConstants.RabbitMqLevel.LEVEL1.equals(RabbitMqCallBackLevelConstants.RABBITMQ_CALL_BACK_LEVEL.get(exchange))) {
            // TODO
            return;
        }

        // 二级打印日志
        if (RabbitMqCallBackLevelConstants.RabbitMqLevel.LEVEL2.equals(RabbitMqCallBackLevelConstants.RABBITMQ_CALL_BACK_LEVEL.get(exchange))) {
            log.info("[RaabitMQ消息失败回调]-消息发送失败 exchangeName[{}], routingKey[{}], 失败状态码[{}], 失败信息[{}], message[{}]",
                    exchange, routingKey, replyCode, replyText, message);
            return;
        }
    }
}

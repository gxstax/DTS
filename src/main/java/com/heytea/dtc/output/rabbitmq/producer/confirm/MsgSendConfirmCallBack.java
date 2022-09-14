package com.heytea.dtc.output.rabbitmq.producer.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * <p>
 * 消息发送确认消息回调
 * </p>
 *
 * @author Ant
 * @since 2022-04-11 18:37
 */
@Slf4j
public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {

    /**
     * <p>
     * 功能描述
     * </p>
     *
     * @param correlationData 失败业务唯一标志
     * @param ack 消息是否发送到交换机
     * @param cause 错误信息
     * @return void
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("[rabbitMQ消息成功确认]-消息发送成功 correlationData[{}].", correlationData);
        } else {
            log.info("[rabbitMQ消息失败确认]-消息发送失败 correlationData[{}], cause[{}].", correlationData, cause);
        }
    }
}

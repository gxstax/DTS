package com.heytea.dtc.output.rabbitmq.producer.mapping;

import com.heytea.dtc.deliver.converters.TransferMessage;
import com.heytea.dtc.output.rabbitmq.producer.message.RabbitMessage;

import java.util.List;

/**
 * <p>
 * rabbit 消息映射
 * </p>
 *
 * @author Ant
 * @since 2022/4/11 9:45 下午
 */
public interface RabbitMqMessageMapping {
    /**
     * <p>
     * 消息映射
     * </p>
     *
     * @param transferMessages
     * @return List<RabbitMessage>
     */
    List<RabbitMessage> doMapping(List<TransferMessage> transferMessages);
}

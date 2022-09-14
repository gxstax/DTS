package com.heytea.dtc.output.rabbitmq.producer;

import com.heytea.dtc.deliver.AbstractDeliver;
import com.heytea.dtc.deliver.converters.TransferMessage;
import com.heytea.dtc.output.rabbitmq.producer.sender.RabbitMqSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * RabbitMq 消息投递
 * </p>
 *
 * @author Ant
 * @since 2022/4/11 9:12 下午
 */
@Slf4j
@Component
public class RabbitMqProducer extends AbstractDeliver {

    private final RabbitMqSender rabbitMqSender;

    public RabbitMqProducer(RabbitMqSender rabbitMqSender) {
        this.rabbitMqSender = rabbitMqSender;
    }

    @Override
    protected boolean deliver(List<TransferMessage> transferMessages) {


        return false;
    }

}

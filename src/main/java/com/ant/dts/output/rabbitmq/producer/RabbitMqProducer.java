package com.ant.dts.output.rabbitmq.producer;

import com.ant.dts.deliver.AbstractDeliver;
import com.ant.dts.deliver.converters.TransferMessage;
import com.ant.dts.output.rabbitmq.producer.sender.RabbitMqSender;
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

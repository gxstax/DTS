package com.ant.dts.output.rabbitmq.producer.message;

import com.ant.dts.deliver.converters.TransferMessage;
import lombok.Getter;

/**
 * <p>
 * RabbitMessage
 * </p>
 *
 * @author Ant
 * @since 2022/4/11 9:46 下午
 */
@Getter
public class RabbitMessage {

    private TransferMessage transferMessage;

    public RabbitMessage() {}

    public RabbitMessage(TransferMessage transferMessage) {
        this.transferMessage = transferMessage;
    }

}

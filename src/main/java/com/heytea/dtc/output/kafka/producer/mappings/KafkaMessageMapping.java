package com.heytea.dtc.output.kafka.producer.mappings;

import com.heytea.dtc.deliver.converters.TransferMessage;
import com.heytea.dtc.output.kafka.producer.message.KafkaMessage;

import java.util.List;

/**
 * <p>
 *  * kafka 消息映射
 * </p>
 *
 * @author Ant
 * @since 2022/1/16 6:51 下午
 */
public interface KafkaMessageMapping {

    List<KafkaMessage> doMapping(List<TransferMessage> transferMessages);

}

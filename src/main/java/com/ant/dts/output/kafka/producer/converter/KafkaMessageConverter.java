package com.ant.dts.output.kafka.producer.converter;

import com.ant.dts.output.kafka.producer.message.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 消息转换器
 * </p>
 *
 * @author Ant
 * @since 2022/1/3 8:46 下午
 */
@Slf4j
public class KafkaMessageConverter {

    public static List<ProducerRecord<String, String>> convertFromCanalValidMessage(List<KafkaMessage> transferMessages) {

        // kafka 消息转换
        List<ProducerRecord<String, String>> producerRecords = new ArrayList<>();
        if (CollectionUtils.isEmpty(transferMessages)) {
            return null;
        }

        for (KafkaMessage message : transferMessages) {
            ProducerRecord producerRecord;
            if (ObjectUtils.isEmpty(message.getPartition())) {
                producerRecord = new ProducerRecord(
                        message.getTopic(),
                        message.getMessageId(),
                        message.getMessageObj()
                );
            } else {
                producerRecord = new ProducerRecord(
                        message.getTopic(),
                        message.getPartition(),
                        message.getMessageId(),
                        message.getMessageObj()
                );
            }

            producerRecords.add(producerRecord);
        }

        return producerRecords;
    }

}

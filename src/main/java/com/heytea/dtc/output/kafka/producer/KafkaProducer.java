package com.heytea.dtc.output.kafka.producer;

import com.heytea.dtc.deliver.AbstractDeliver;
import com.heytea.dtc.deliver.converters.TransferMessage;
import com.heytea.dtc.exception.DTCException;
import com.heytea.dtc.output.kafka.producer.converter.KafkaMessageConverter;
import com.heytea.dtc.output.kafka.producer.mappings.KafkaMessageMapping;
import com.heytea.dtc.output.kafka.producer.message.KafkaMessage;
import com.heytea.dtc.output.kafka.producer.sender.KafkaMessageProducerSender;
import com.heytea.dtc.output.kafka.producer.sender.MessageProducerSender;
import com.heytea.dtc.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Kafka 消息投递
 * </p>
 *
 * @author Ant
 * @since 2022/1/3 6:59 下午
 */
@Slf4j
@Component
public class KafkaProducer extends AbstractDeliver {

    private final List<KafkaMessageMapping> kafkaMessageMappings;

    public KafkaProducer(List<KafkaMessageMapping> kafkaMessageMappings) {
        this.kafkaMessageMappings = kafkaMessageMappings;
    }

    @Override
    protected boolean deliver(List<TransferMessage> transferMessages) {
        // kafka 消息提取
        List<ProducerRecord<String, String>> producerRecords = extractProducerRecords(transferMessages);
        if (CollectionUtils.isEmpty(producerRecords)) {
            return true;
        }

        // 拿到kafka 生产者实例对象
        final MessageProducerSender messageProducerSender = (MessageProducerSender) SpringContextUtil.getBean(KafkaMessageProducerSender.class);

        // kafka 消息发送（这里我们不去拿发送结果，请移步到 callBack 对象同一处理）
        List<Future> futures = new ArrayList<>(producerRecords.size());
        for (ProducerRecord producerRecord : producerRecords) {
            final Future<RecordMetadata> sendResult = messageProducerSender.send(producerRecord);
            futures.add(sendResult);
        }

        for (Future future : futures) {
            try {
                final RecordMetadata recordMetadata = (RecordMetadata) future.get(3, TimeUnit.SECONDS);
                if (ObjectUtils.isEmpty(recordMetadata)) {
                    log.warn("消息发送kafka失败");
                    throw new DTCException("消息发送到Kafka失败");
                }
            } catch (Exception e) {
                log.warn("消息发送kafka失败", e);
                throw new DTCException("消息发送到Kafka失败");
            }
        }

        return true;
    }

    /**
     * <p>
     * 提取 kafka 消息
     * </p>
     *
     * @param transferMessages
     * @return producerRecords
     */
    private List<ProducerRecord<String, String>> extractProducerRecords(List<TransferMessage> transferMessages) {
        // 消息映射
        List<KafkaMessage> kafkaMessages = new ArrayList<>();
        for (KafkaMessageMapping kafkaMessageMapping : kafkaMessageMappings) {
            final List<KafkaMessage> filterKafkaMessages = kafkaMessageMapping.doMapping(transferMessages);
            kafkaMessages.addAll(filterKafkaMessages);
        }

        // 转换为 kafka 消息格式
        List<ProducerRecord<String, String>> producerRecords =
                KafkaMessageConverter.convertFromCanalValidMessage(kafkaMessages);

        return producerRecords;
    }

}

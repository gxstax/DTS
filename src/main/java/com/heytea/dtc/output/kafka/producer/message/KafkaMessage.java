package com.heytea.dtc.output.kafka.producer.message;

import lombok.Data;

/**
 * <p>
 * Kafka 消息对象
 * </p>
 *
 * @author Ant
 * @since 2022/1/7 8:41 下午
 */
@Data
public class KafkaMessage {

    private String messageId;
    private String topic;
    private Integer partition;
    private byte[] messageByte;
    private Object messageObj;


    public KafkaMessage(String topic, Object messageObj) {
        this.topic = topic;
        this.messageObj = messageObj;
    }

    public KafkaMessage(String topic, String tableName, Object messageObj) {
        this.messageId = tableName;
        this.topic = topic;
        this.messageObj = messageObj;
    }

    public KafkaMessage(String topic, Integer partition, String tableName, Object messageObj) {
        this.messageId = tableName;
        this.topic = topic;
        this.partition = partition;
        this.messageObj = messageObj;
    }

}


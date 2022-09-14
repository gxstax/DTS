package com.ant.dts.output.kafka.producer.sender;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Future;

/**
 * <p>
 * 消息发送
 * </p>
 *
 * @author Ant
 * @since 2022/1/4 3:12 下午
 */
public interface MessageProducerSender<K, V> {

    /**
     * 发送消息
     *
     * <p>
     *  发消息最好适用带有回调的方式发送
     *  -- 这里即使你没有传 callBack 也会构建一个默认的 CallBack 对象
     *     不建议使用无 CallBack 参数的 API
     * </p>
     *
     * @param record
     * @return {@link RecordMetadata}
     */
    Future<RecordMetadata> send(ProducerRecord<K, V> record);

    /**
     * <p>
     * 发送消息
     * </p>
     *
     * @param record
     * @param callback 回调对象
     * @return
     */
    Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback);
}

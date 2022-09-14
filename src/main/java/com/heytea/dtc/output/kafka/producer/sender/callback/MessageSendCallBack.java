package com.heytea.dtc.output.kafka.producer.sender.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * <p>
 * 生产端消息发送回调
 * </p>
 *
 * @author Ant
 * @since 2022/1/4 3:33 下午
 */
@Slf4j
public class MessageSendCallBack implements Callback {

    /**
     * 异步发送消息回调
     *
     * @param metadata  已发送信息的元数据信息，如果发生异常，则元数据信息除了topicPartition 字段，其余为空
     * @param exception 如果发送无异常，则 exception 是  Null，如果有异常，则是对应的异常信息
     */
    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (metadata != null) {
            // 成功
            // TODO
            log.info("消息发送成功了 metadata[{}]", metadata);
        }

        if (null != exception) {
            // 失败
            // TODO
            log.warn("哦豁，消息发送失败了", exception);
        }
    }

}

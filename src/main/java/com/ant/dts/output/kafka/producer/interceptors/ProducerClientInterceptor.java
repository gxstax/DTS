package com.ant.dts.output.kafka.producer.interceptors;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * <p>
 * 生产者客户端拦截器接口
 * </p>
 *
 * @author Ant
 * @since 2021/12/27 1:11 下午
 */
public interface ProducerClientInterceptor extends ProducerInterceptor {

    /**
     * <p>
     * 生产者消息发送之前处理器
     * </p>
     *
     * @param record
     * @return producer record to send to topic/partition
     */
    @Override
    default ProducerRecord onSend(ProducerRecord record) {
        // 默认不做任何处理，返回原始数据
        return record;
    }

    /**
     * 消息成功提交或发送失败之后被调用
     *
     * <p>
     *  1. 和 onSend() 方法不在同一线程，注意线程安全
     *  2. 该方法处在 Producer 发送的主路径中，所以最好别放一些太重的逻辑进去，
     *     否则你会发现你的 Producer TPS 直线下降
     * </p>
     *
     * @param metadata
     * @param exception
     * @return void
     */
    @Override
    default void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    /**
     * <p>
     * 拦截器关闭回调
     * </p>
     *
     */
    @Override
    default void close() {
    }

    /**
     * <p>
     * 配置键值对信息
     * </p>
     *
     * @param configs
     */
    @Override
    default void configure(Map<String, ?> configs) {
    }

}

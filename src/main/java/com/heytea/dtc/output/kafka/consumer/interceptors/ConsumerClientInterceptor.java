package com.heytea.dtc.output.kafka.consumer.interceptors;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Map;

/**
 * <p>
 * 消费者客户端拦截器接口
 * </p>
 *
 * @author Ant
 * @since 2021/12/27 1:40 下午
 */
public interface ConsumerClientInterceptor extends ConsumerInterceptor<String, String> {
    /**
     *
     * <p>
     * 消息返回给 Consumer 程序之前调用
     * {@link KafkaConsumer#poll(long)}
     * <p>
     *
     * @param records
     * @return
     */
    @Override
    default ConsumerRecords onConsume(ConsumerRecords<String, String> records) {
        // 默认不做任何处理，返回原始数据
        return records;
    }

    /**
     *
     * <p>
     * 提交位移之后调用该方法
     * 1. 通常你可以在该方法中做一些记账类的动作，比如打日志等。
     * </p>
     *
     * @param offsets 偏移量
     */
    @Override
    default void onCommit(Map offsets) {
    }

    /**
     * <p>
     * 拦截器关闭回调
     * </p>
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

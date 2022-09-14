package com.ant.dts.output.kafka.producer.interceptors;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * <p>
 * 端到端的延时统计拦截器
 * </p>
 *
 * @author Ant
 * @since 2021/12/27 1:10 下午
 */
public class AvgLatencyProducerInterceptor implements ProducerClientInterceptor {

    /**
     * <p>
     * 生产者消息发送之前处理器
     * </p>
     *
     * @param record
     * @return producer record to send to topic/partition
     */
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        // 发送数据数量统计
//        JedisPoolUtil.incr("totalSendMessages");
        return ProducerClientInterceptor.super.onSend(record);
    }

    /**
     * <p>
     * 消息成功提交或发送失败之后被调用
     * 1. 和 onSend() 方法不在同一线程，注意线程安全
     * 2. 该方法处在 Producer 发送的主路径中，所以最好别放一些太重的逻辑进去，否则你会发现你的 Producer TPS 直线下降
     * </p>
     *
     * @param metadata
     * @param exception Null if no error occurred
     * @return void
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        ProducerClientInterceptor.super.onAcknowledgement(metadata, exception);
    }

}

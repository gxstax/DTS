package com.ant.dts.output.kafka.consumer.interceptors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Map;

/**
 * <p>
 * 端到端的延时统计拦截器
 * </p>
 *
 * @author Ant
 * @since 2021/12/27 8:29 下午
 */
public class AvgLatencyConsumerInterceptor implements ConsumerClientInterceptor {
    /**
     * <p>
     * 消息返回给 Consumer 程序之前调用
     * {@link KafkaConsumer#poll(long)}
     * <p>
     *
     * @param records
     * @return
     */
    @Override
    public ConsumerRecords onConsume(ConsumerRecords<String, String> records) {
        // 计算延迟时间
        long latency = 0L;
        for (ConsumerRecord record : records) {
            latency += (System.currentTimeMillis() - record.timestamp());
        }
//        final Jedis jedis = JedisPoolUtil.getJedis();
//        jedis.incrBy("totalLatency", latency);
//
//        final long totalLatency = Long.parseLong(jedis.get("totalLatency"));
//        final long totalSendMessages = Long.parseLong(jedis.get("totalSendMessages"));
//
//        jedis.set("avgLatency", String.valueOf(totalLatency/totalSendMessages));

        return records;
    }

    /**
     * <p>
     * 提交位移之后调用该方法
     * 1. 通常你可以在该方法中做一些记账类的动作，比如打日志等。
     * </p>
     *
     * @param offsets 偏移量
     */
    @Override
    public void onCommit(Map offsets) {
    }

}

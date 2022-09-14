package com.ant.dts.output.kafka.producer.partitioner.selector;

import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 轮询分区策略
 * </p>
 *
 * @author Ant
 * @since 2022/1/13 2:52 下午
 */
public class RoundRobinPartitionSelector extends AbstractPartitionSelector {

    private final ConcurrentMap<String, AtomicInteger> topicCounterMap = new ConcurrentHashMap<>();

    @Override
    public Object select(Object resource) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        int nextValue = nextValue(topic);
        List<PartitionInfo> availablePartitions = cluster.availablePartitionsForTopic(topic);
        if (!availablePartitions.isEmpty()) {
            int part = Utils.toPositive(nextValue) % availablePartitions.size();
            return availablePartitions.get(part).partition();
        } else {
            // no partitions are available, give a non-available partition
            return Utils.toPositive(nextValue) % numPartitions;
        }
    }

    private int nextValue(String topic) {
        AtomicInteger counter = topicCounterMap.computeIfAbsent(topic, k -> {
            return new AtomicInteger(0);
        });
        return counter.getAndIncrement();
    }

}

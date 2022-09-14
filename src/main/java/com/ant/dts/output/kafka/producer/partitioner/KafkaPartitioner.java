package com.ant.dts.output.kafka.producer.partitioner;

import com.ant.dts.output.kafka.producer.partitioner.selector.PartitionSelector;
import com.ant.dts.resources.partition.TopicPartitionRule;
import com.ant.dts.util.SpringContextUtil;
import com.ant.dts.util.collections.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义分区策略
 * </p>
 *
 * @author Ant
 * @since 2022/1/7 9:27 下午
 */
@Slf4j
public class KafkaPartitioner implements Partitioner {

    /**
     * Compute the partition for the given record.
     *
     * @param topic      The topic name
     * @param key        The key to partition on (or null if no key)
     * @param keyBytes   The serialized key to partition on( or null if no key)
     * @param value      The value to partition on or null
     * @param valueBytes The serialized value to partition on or null
     * @param cluster    The current cluster metadata
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 拿主题分区配置信息
        final List<TopicPartitionRule> topicPartitionRule = SpringContextUtil.getTopicPartitionRule();
        final Map<String, TopicPartitionRule> topicPartitionMap = ListUtil.extractMap(topicPartitionRule, TopicPartitionRule::getTopic);

        // 分区信息
        final List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);

        // 分区规则
        final TopicPartitionRule topicRule = topicPartitionMap.get(topic);

        // 分区选择器
        PartitionSelector partitionSelector = PartitionSelector.getPartitionSelector(topic, topicRule.getRouteRule(), cluster);

        return (Integer) partitionSelector.select(value);
    }

    /**
     * This is called when partitioner is closed.
     */
    @Override
    public void close() {

    }

    /**
     * Configure this class with the given key-value pairs
     *
     * @param configs
     */
    @Override
    public void configure(Map<String, ?> configs) {
    }

}

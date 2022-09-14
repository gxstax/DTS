package com.ant.dts.output.kafka.producer.partitioner.selector;

import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * 功能描述
 * </p>
 *
 * @author Ant
 * @since 2022/1/13 5:13 下午
 */
public class RandomPartitionSelector extends AbstractPartitionSelector {

    @Override
    public Object select(Object resource) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        return ThreadLocalRandom.current().nextInt(partitions.size());
    }

}

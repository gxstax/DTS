package com.heytea.dtc.output.kafka.producer.partitioner.selector;

import org.apache.kafka.common.Cluster;

/**
 * <p>
 * 分区选择器
 * </p>
 *
 * @author Ant
 * @since 2022/1/13 3:40 下午
 */
public class AbstractPartitionSelector implements PartitionSelector {

    protected String topic;

    protected String routeRule;

    protected Cluster cluster;

    public AbstractPartitionSelector() {
    }


    @Override
    public Object select(Object resource) {
        return null;
    }

    protected PartitionSelector initProperties(String topic, String routeRule, Cluster cluster) {
        this.topic = topic;
        this.routeRule = routeRule;
        this.cluster = cluster;
        return this;
    }

}

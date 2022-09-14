package com.ant.dts.output.kafka.producer.partitioner.selector;

import com.ant.dts.constant.enums.RouteType;
import org.apache.kafka.common.Cluster;

/**
 * <p>
 * 分区选择器
 * </p>
 *
 * @author Ant
 * @since 2022/1/13 10:35 上午
 */
public interface PartitionSelector<T, R> {

    // 字段路由分区器
    ColumnAssignPartitionSelector columnAssignPartitionSelector = new ColumnAssignPartitionSelector();
    // 轮询分区器
    RoundRobinPartitionSelector roundRobinPartitionSelector = new RoundRobinPartitionSelector();
    // 随机分区器
    RandomPartitionSelector randomPartitionSelector = new RandomPartitionSelector();

    static PartitionSelector getPartitionSelector(String topic, String routeRule, Cluster cluster) {
        // 字段值hash分区策略
        if (RouteType.isColumnRule(routeRule)) {
            return columnAssignPartitionSelector.initProperties(topic, routeRule, cluster);
        }

        // 轮询分区策略
        if (RouteType.isRoundRobinRule(routeRule)) {
            return roundRobinPartitionSelector.initProperties(topic, routeRule, cluster);
        }

        // 随机分区策略
        if (RouteType.isRoundRobinRule(routeRule)) {
            return randomPartitionSelector.initProperties(topic, routeRule, cluster);
        }

        // 默认轮询
        return roundRobinPartitionSelector.initProperties(topic, routeRule, cluster);
    }

    T select(R resource);

}

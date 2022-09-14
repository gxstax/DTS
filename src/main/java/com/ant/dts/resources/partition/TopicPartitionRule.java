package com.ant.dts.resources.partition;

import lombok.Getter;

/**
 * <p>
 * 主题分区规则
 * </p>
 *
 * @author Ant
 * @since 2022/1/12 12:56 下午
 */
@Getter
public class TopicPartitionRule {

    /**
     * 主题
     */
    private String topic;

    /**
     * 路由规则
     */
    private String routeRule;

    public TopicPartitionRule(String topic, String routeRule) {
        this.topic = topic;
        this.routeRule = routeRule;
    }

}

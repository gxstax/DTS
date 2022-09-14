package com.heytea.dtc.resources.mapping.kafka;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lijunjun
 * @date 2021-11-3 15:05
 */
@Getter
public class TableMapping{
    /**
     * 表名称
     */
    private String table;
    /**
     * sharding配置因子
     */
    private String shardingFactor;

    /**
     * 主题列表
     */
    private List<TopicMapping> topics;

    private Map<String, TopicMapping> topicMappingMap = new ConcurrentHashMap<>();

    private boolean sharding = false;

    /**
     * <p>
     * 初始化延伸属性
     * </p>
     *
     * @param
     * @return void
     */
    public void initExtendedProperties() {
        this.topics.stream().filter(Objects::nonNull).forEach(topicMapping -> {
            topicMappingMap.put(topicMapping.getTopic(), topicMapping);
            // 初始化延伸属性
            topicMapping.initExtendedProperties();
        });
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setShardingFactor(String shardingFactor) {
        this.shardingFactor = shardingFactor;
        if (StringUtils.isNotEmpty(shardingFactor))
            this.sharding = true;
    }

    public void setTopics(List<TopicMapping> topics) {
        this.topics = topics;
        if (topics != null && topics.size() > 0)
            topicMappingMap = topics.stream().collect(
                    Collectors.toConcurrentMap(
                            o -> o.getTopic(),
                            Function.identity()
                    )
            );
    }

}

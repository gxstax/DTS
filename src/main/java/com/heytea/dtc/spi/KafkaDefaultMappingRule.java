package com.heytea.dtc.spi;

import com.heytea.dtc.resources.mapping.kafka.DBMapping;
import com.heytea.dtc.resources.mapping.kafka.TableMapping;
import com.heytea.dtc.resources.mapping.kafka.TopicMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 功能描述
 * </p>
 *
 * @author Ant
 * @since 2022/1/3 10:34 下午
 */
@Deprecated
public class KafkaDefaultMappingRule implements CanalKafkaMessageMappingRule {

    /**
     * <p>
     * 映射规则
     * </p>
     *
     * @param dbMapping
     * @return void
     */
    @Override
    public DBMapping mappingRule(DBMapping dbMapping) {
        dbMapping = new DBMapping();
        dbMapping.setDb("heytea");
        List<TableMapping> tables = new ArrayList<>();
        dbMapping.setTables(tables);
        TableMapping tableMapping = new TableMapping();
        tables.add(tableMapping);

        tableMapping.setTable("user");
        List<TopicMapping> topics = new ArrayList<>();
        tableMapping.setTopics(topics);

        TopicMapping topicMapping = new TopicMapping();
        topics.add(topicMapping);
        // 设置对应主题名
        topicMapping.setTopic("heytea-user-table");
//        topicMapping.setPartitions(3);
        topicMapping.setColumn("name");
        topicMapping.setEventType("INSERT,UPDATE");
//        topicMapping.setRoute("COLUMN[id]");

        return dbMapping;
    }

}

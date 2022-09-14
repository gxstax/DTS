package com.ant.dts.resources;

import com.ant.dts.exception.DTCException;
import com.ant.dts.resources.filters.FieldFilter;
import com.ant.dts.resources.mapping.kafka.DBMapping;
import com.ant.dts.resources.mapping.kafka.TableMapping;
import com.ant.dts.resources.mapping.kafka.TopicMapping;
import com.ant.dts.resources.mapping.rabbitmq.RabbitMqDbMapping;
import com.ant.dts.resources.partition.TopicPartitionRule;
import com.ant.dts.resources.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 资源加载
 * </p>
 *
 * @author Ant
 * @since 2022/1/6 8:03 下午
 */
@Slf4j
@Component
public class ResourceStore implements ApplicationContextAware {

    /** Kafak-数据库映射配置文件路径 **/
    private final static String kafkaDbMappingPath = "classpath*:/META-INF/dbmapping/kafka/*.yml";

    /** RabbitMq-数据库映射配置文件路径 **/
    private final static String rabbitDbMappingPath = "classpath*:/META-INF/dbmapping/rabbit/*.yml";

    /** 字段过滤配置文件路径 **/
    private final static String fieldFilterPath = "classpath*:/META-INF/filter/*.yml";

    /** 字段过滤配置文件路径 **/
    private final static String kafkaPartitionPath = "classpath*:/META-INF/partition/*.yml";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Cacheable(value = "topic-partition-rule", key = "'partition-rule'", sync = true)
    public List<TopicPartitionRule> getPartitionRule() {
        List<TopicPartitionRule> topicPartitionRules = new ArrayList<>();
        final List<TopicMapping> topicMappings = getTopics();
        for (TopicMapping topicMapping : topicMappings) {
            TopicPartitionRule topicPartitionRule =
                    new TopicPartitionRule(topicMapping.getTopic(), topicMapping.getRoute());
            topicPartitionRules.add(topicPartitionRule);
        }

        return topicPartitionRules;
    }

    @Cacheable(value = "kafka-topic-config", key = "'topic-config'", sync = true)
    public List<TopicMapping> getTopics() {
        List<TopicMapping> topicMappings = new ArrayList<>();
        List<DBMapping> dbMappings = loadKafkaDBMapping();
        dbMappings.stream().forEach(dbMapping -> {
            for (TableMapping table : dbMapping.getTables()) {
                topicMappings.addAll(table.getTopics());
            }
        });
        return topicMappings;
    }

    @Cacheable(value = "field-filter-config", key = "'field-filter'", sync = true)
    public List<FieldFilter> getFieldFilters() {
        return loadFieldFilter();
    }

    @Cacheable(value = "kafka-db-mapping-config", key = "'db-mapping'", sync = true)
    public List<DBMapping> loadKafkaDBMapping() {
        List<DBMapping> dbMappings = new ArrayList<>();

        Resource[] resources = null;
        try {
            resources = applicationContext.getResources(kafkaDbMappingPath);
        } catch (IOException e) {
            log.warn("加载数据库映射信息异常", e);
            throw new DTCException("加载数据库映射信息异常", e);
        }

        Yaml yaml = new Yaml(new Constructor(DBMapping.class));
        for (Resource resource : resources) {
            final DBMapping dbMapping = yaml.load(ResourceUtil.getContent(resource));
            // 初始化额外属性
            dbMapping.initExtendedProperties();
            dbMappings.add(dbMapping);
        }

        return dbMappings;
    }

    @Cacheable(value = "rabbit-db-mapping-config", key = "'db-mapping'", sync = true)
    public List<RabbitMqDbMapping> loadRabbitDBMapping() {
        List<RabbitMqDbMapping> dbMappings = new ArrayList<>();

        Resource[] resources = null;
        try {
            resources = applicationContext.getResources(rabbitDbMappingPath);
        } catch (IOException e) {
            log.warn("加载Rabbit-数据库映射信息异常", e);
            throw new DTCException("加载Rabbit-数据库映射信息异常", e);
        }

        Yaml yaml = new Yaml(new Constructor(DBMapping.class));
        for (Resource resource : resources) {
            final RabbitMqDbMapping dbMapping = yaml.load(ResourceUtil.getContent(resource));
            // 初始化额外属性
            dbMapping.initExtendedProperties();
            dbMappings.add(dbMapping);
        }

        return dbMappings;
    }



    public List<FieldFilter> loadFieldFilter() {
        List<FieldFilter> fieldFilters = new ArrayList<>();

        Resource[] resources;
        try {
            resources = applicationContext.getResources(fieldFilterPath);
        } catch (IOException e) {
            log.warn("加载字段过滤信息异常", e);
            throw new DTCException("加载字段过滤信息异常", e);
        }

        Yaml yaml = new Yaml(new Constructor(FieldFilter.class));
        for (Resource resource : resources) {
            final FieldFilter fieldFilter = yaml.load(ResourceUtil.getContent(resource));
            fieldFilters.add(fieldFilter);
        }

        return fieldFilters;
    }

}

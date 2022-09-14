package com.heytea.dtc.output.kafka.admin;

import com.heytea.dtc.config.ApplicationConfig;
import com.heytea.dtc.resources.ResourceStore;
import com.heytea.dtc.spi.CanalKafkaMessageMappingRule;
import com.heytea.dtc.resources.mapping.kafka.DBMapping;
import com.heytea.dtc.resources.mapping.kafka.TableMapping;
import com.heytea.dtc.resources.mapping.kafka.TopicMapping;
import com.heytea.dtc.spi.serviceloader.CanalServiceLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

/**
 * <p>
 *  kafka 主题管理客户端：包括主题的创建、删除和查询。
 * </p>
 *
 * @author Ant
 * @since 2022/1/5 11:08 上午
 */
@Slf4j
@Component
public class KafkaTopicAdminClient extends AbstracAdminClient {

    public KafkaTopicAdminClient(ApplicationConfig applicationConfig,
                                 ResourceStore resourceStore) {
        super(applicationConfig.getKafkaCommonConfig(), applicationConfig.getKafkaAdminConfig());
        List<TopicMapping> topics = resourceStore.getTopics();
        initTopic(topics);
    }

    /**
     * <p>
     * 初始化TOPIC
     * </p>
     *
     * @param topics
     * @return void
     */
    private void initTopic(List<TopicMapping> topics) {
        if (CollectionUtils.isEmpty(topics)) {
            return;
        }
        // 获取已经存在的主题列表
        final Set<String> topicNames = listTopicNameSet();

        List<NewTopic> newTopics = new ArrayList<>();
        for (TopicMapping topic : topics) {
            final String addTopicName = topic.getTopic();
            // 判断主题是否已经存在或未配置
            if (topicNames.contains(topic.getTopic()) || StringUtils.isEmpty(addTopicName)) {
                continue;
            }
            // 分区数
            final Integer partitions = null != topic.getPartitions() ? topic.getPartitions() : 3;
            // 分区副本数
            final short partitionReplicas = 1;

            NewTopic newTopic = new NewTopic(addTopicName, partitions, partitionReplicas);
            newTopics.add(newTopic);
        }
        if (!CollectionUtils.isEmpty(newTopics)) {
            createTopics(newTopics);
        }

    }

    @Cacheable(value = "kafka-topic")
    public List<TopicMapping> getTopics() {
        List<TopicMapping> topicMappings = new ArrayList<>();
        ServiceLoader<CanalKafkaMessageMappingRule> loader = CanalServiceLoader.loadAll(CanalKafkaMessageMappingRule.class);
        StreamSupport.stream(loader.spliterator(), false).forEach(s -> {
            final DBMapping dbMapping = s.mappingRule(new DBMapping());
            for (TableMapping table : dbMapping.getTables()) {
                topicMappings.addAll(table.getTopics());
            }
        });
        return topicMappings;
    }

    /**
     * <p>
     * 创建主题
     * </p>
     *
     * @param topic 主题名称
     * @param partitions 分区数
     * @param partitionReplicas 分区副本数
     * @return boolean
     */
    public boolean createTopics(String topic, int partitions, short partitionReplicas) {
        NewTopic newTopic = new NewTopic(topic, partitions, partitionReplicas);
        final List<NewTopic> newTopics = Arrays.asList(newTopic);
        return createTopics(newTopics);
    }

    /**
     * <p>
     * 创建主题
     * </p>
     *
     * @param newTopics
     * @return boolean
     */
    public boolean createTopics(List<NewTopic> newTopics) {
        final AdminClient adminClient = getAdminClient();
        final CreateTopicsResult result = adminClient.createTopics(newTopics);
        try {
            result.all().get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("[kafka创建主题失败], newTopics[{}]", newTopics, e);
            return false;
        }
        return true;
    }

    /**
     * <p>
     * 查询 kafka 主题名称列表信息
     * </p>
     *
     * @param
     * @return {@link ListTopicsResult}
     */
    public Set<String> listTopicNameSet() {
        final ListTopicsResult listTopicsResult = listTopics();
        final KafkaFuture<Set<String>> nameFuture = listTopicsResult.names();

        final Set<String> topicNameSet;
        try {
            topicNameSet = nameFuture.get();
        } catch (Exception e) {
            log.warn("查询主题信息失败", e);
            return null;
        }
        return topicNameSet;
    }

    /**
     * <p>
     * 查询 kafka 主题列表信息
     * </p>
     *
     * @param
     * @return {@link ListTopicsResult}
     */
    public ListTopicsResult listTopics() {
        final AdminClient adminClient = getAdminClient();
        final ListTopicsResult listTopicsResult = adminClient.listTopics();
        return listTopicsResult;
    }

    /**
     * <p>
     * 查询 kafka 主题列表信息
     * </p>
     *
     * @param options {@link ListTopicsOptions#timeoutMs(Integer ms)} : 请求超时时间
     *                {@link ListTopicsOptions#listInternal(boolean listInternal)} : 是否列出内部主题
     *
     * @return {@link ListTopicsResult}
     */
    public ListTopicsResult listTopics(final ListTopicsOptions options) {
        final AdminClient adminClient = getAdminClient();
        return adminClient.listTopics(options);
    }


}

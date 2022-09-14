package com.heytea.dtc.output.kafka.producer.mappings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heytea.dtc.constant.enums.EventType;
import com.heytea.dtc.deliver.converters.TransferMessage;
import com.heytea.dtc.output.kafka.producer.message.KafkaMessage;
import com.heytea.dtc.resources.ResourceStore;
import com.heytea.dtc.resources.mapping.kafka.DBMapping;
import com.heytea.dtc.resources.mapping.kafka.TableMapping;
import com.heytea.dtc.resources.mapping.kafka.TopicMapping;
import com.heytea.dtc.util.RegularUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * topic-Db 规则映射
 * </p>
 *
 * @author Ant
 * @since 2022/1/16 6:52 下午
 */
@Slf4j
@Component
public class KafkaDbMessageMapping implements KafkaMessageMapping {

    private final ResourceStore resourceStore;

    public KafkaDbMessageMapping(ResourceStore resourceStore) {
        this.resourceStore = resourceStore;
    }

    @Override
    public List<KafkaMessage> doMapping(List<TransferMessage> transferMessages) {

        List<KafkaMessage> kafkaMessages = new ArrayList<>();

        // 加载映射规则信息
        final List<DBMapping> dbMappings = resourceStore.loadKafkaDBMapping();
        // 规则过滤
        Iterator<TransferMessage> messageIte = transferMessages.stream().iterator();
        while (messageIte.hasNext()) {
            TransferMessage message = messageIte.next();
            List<KafkaMessage> batchKafkaMessages = filterMessage(message, dbMappings);
            if (!CollectionUtils.isEmpty(batchKafkaMessages)) {
                kafkaMessages.addAll(batchKafkaMessages);
            }
        }
        return kafkaMessages;
    }

    private List<KafkaMessage> filterMessage(TransferMessage message, List<DBMapping> dbMappings) {
        List<KafkaMessage> kafkaMessages = new ArrayList<>();
        final Iterator<DBMapping> dbMappingIte = dbMappings.iterator();
        while (dbMappingIte.hasNext()) {
            final DBMapping dbMapping = dbMappingIte.next();
            // 库是否匹配
            if (dbMapping.getDb().equals(message.getDb())) {
                // 匹配表
                final List<TableMapping> tableMappings = dbMapping.getTables();
                if (!CollectionUtils.isEmpty(tableMappings)) {
                    for (TableMapping tableMapping : tableMappings) {
                        // 正则匹配
                        if (RegularUtil.isMatchTable(tableMapping.getTable(), message.getTable())) {
                            final List<TopicMapping> topics = tableMapping.getTopics();
                            if (!CollectionUtils.isEmpty(topics)) {
                                // 匹配主题
                                kafkaMessages = filterTopics(topics, message);
                            }
                        }
                    }
                }
            }
        }

        return kafkaMessages;
    }

    /**
     * <p>
     * 主题匹配
     * </p>
     *
     * @param topics
     * @param message
     * @return KafkaMessages
     */
    private List<KafkaMessage> filterTopics(List<TopicMapping> topics, TransferMessage message) {
        List<KafkaMessage> kafkaMessages = new ArrayList<>();
        for (TopicMapping topic : topics) {

            TransferMessage.TransferData transferData = message.getTransferData();
            Map<String, TransferMessage.ColumnInfo> columeInfo = transferData.getColume();
            // 提取出目标字段
            extractTargetColumns(topic.getTargetColumns(), columeInfo);

            // 是否是需要监听的事件
            if (!EventType.isConfigEvent(topic.getEventType(), message.getEventType())) {
                continue;
            }

            // 获取配置监听变化的列信息
            final String[] columnArr = topic.getColumnArr();
            for (String column : columnArr) {
                // 如果变动列包含监控列，并且是有变化的
                if (columeInfo.containsKey(column)) {
                    if (columeInfo.get(column).isChanged()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String messageStr = "";
                        try {
                            messageStr = objectMapper.writeValueAsString(message);
                        } catch (JsonProcessingException e) {
                            log.warn("消息序列化失败", e);
                        }

                        // 构建 kafkaMessage 对象
                        KafkaMessage kafkaMessage;
                        if (ObjectUtils.isEmpty(topic.getAssignPartition())) {
                            kafkaMessage = new KafkaMessage(topic.getTopic(), message.getTable(), messageStr);
                        } else {
                            kafkaMessage = new KafkaMessage(topic.getTopic(), topic.getAssignPartition(), message.getTable(), messageStr);
                        }

                        kafkaMessages.add(kafkaMessage);
                        break;
                    }
                }
            }

        }
        return kafkaMessages;
    }

    /**
     * <p>
     * 去掉
     * </p>
     *
     * @param targetColumns
     * @param columnInfoMap
     * @return void
     */
    private void extractTargetColumns(Set<String> targetColumns, Map<String, TransferMessage.ColumnInfo> columnInfoMap) {

        final Iterator<Map.Entry<String, TransferMessage.ColumnInfo>> iterator = columnInfoMap.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, TransferMessage.ColumnInfo> next = iterator.next();
            final String key = next.getKey();
            if (!targetColumns.contains(key)) {
                iterator.remove();
            }
        }
    }
}

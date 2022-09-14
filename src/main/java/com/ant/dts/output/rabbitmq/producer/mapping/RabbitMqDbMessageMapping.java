package com.ant.dts.output.rabbitmq.producer.mapping;

import com.ant.dts.constant.enums.EventType;
import com.ant.dts.deliver.converters.TransferMessage;
import com.ant.dts.output.rabbitmq.producer.message.RabbitMessage;
import com.ant.dts.resources.ResourceStore;
import com.ant.dts.resources.mapping.rabbitmq.RabbitMqDbMapping;
import com.ant.dts.util.RegularUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * rabbit-Db 规则映射
 * </p>
 *
 * @author Ant
 * @since 2022/4/11 9:47 下午
 */
@Slf4j
@Component
public class RabbitMqDbMessageMapping implements RabbitMqMessageMapping {

    private final ResourceStore resourceStore;

    public RabbitMqDbMessageMapping(ResourceStore resourceStore) {
        this.resourceStore = resourceStore;
    }

    /**
     * <p>
     * 消息映射
     * </p>
     *
     * @param transferMessages
     * @return List<RabbitMessage>
     */
    @Override
    public List<RabbitMessage> doMapping(List<TransferMessage> transferMessages) {
        List<RabbitMessage> rabbitMessages = new ArrayList<>();
        // 加载映射规则信息
        final List<RabbitMqDbMapping> rabbitMqDbMappings = resourceStore.loadRabbitDBMapping();

        Iterator<TransferMessage> messageIte = transferMessages.stream().iterator();
        while (messageIte.hasNext()) {
            TransferMessage message = messageIte.next();
            List<RabbitMessage> batchRabbitMessages = filterMessage(message, rabbitMqDbMappings);
            if (!CollectionUtils.isEmpty(batchRabbitMessages)) {
                rabbitMessages.addAll(batchRabbitMessages);
            }
        }
        return rabbitMessages;
    }

    private List<RabbitMessage> filterMessage(TransferMessage message, List<RabbitMqDbMapping> dbMappings) {
        List<RabbitMessage> rabbitMessages = new ArrayList<>();
        final Iterator<RabbitMqDbMapping> dbMappingIte = dbMappings.iterator();
        while (dbMappingIte.hasNext()) {
            final RabbitMqDbMapping dbMapping = dbMappingIte.next();
            // 库是否匹配
            if (dbMapping.getDb().equals(message.getDb())) {
                // 匹配表
                final List<RabbitMqDbMapping.TableMapping> tableMappings = dbMapping.getTables();
                if (!CollectionUtils.isEmpty(tableMappings)) {
                    for (RabbitMqDbMapping.TableMapping tableMapping : tableMappings) {
                        // 正则匹配
                        if (RegularUtil.isMatchTable(tableMapping.getTable(), message.getTable())) {
                            final List<RabbitMqDbMapping.ExchangeMapping> exchanges = tableMapping.getExchanges();
                            if (!CollectionUtils.isEmpty(exchanges)) {
                                // 匹配交换机
                                rabbitMessages = filterExchanges(exchanges, message);
                            }
                        }
                    }
                }
            }
        }

        return rabbitMessages;
    }

    /**
     * <p>
     * 交换机匹配
     * </p>
     *
     * @param exchanges
     * @param message
     * @return RabbitMessages
     */
    private List<RabbitMessage> filterExchanges(List<RabbitMqDbMapping.ExchangeMapping> exchanges, TransferMessage message) {
        List<RabbitMessage> rabbitMessages = new ArrayList<>();
        for (RabbitMqDbMapping.ExchangeMapping exchange : exchanges) {

            TransferMessage.TransferData transferData = message.getTransferData();
            Map<String, TransferMessage.ColumnInfo> columeInfo = transferData.getColume();
            // 提取出目标字段
            extractTargetColumns(exchange.getTargetColumns(), columeInfo);

            // 是否是需要监听的事件
            if (!EventType.isConfigEvent(exchange.getEventType(), message.getEventType())) {
                continue;
            }

            // 获取配置监听变化的列信息
            final String[] columnArr = exchange.getColumnArr();
            for (String column : columnArr) {
                // 如果变动列包含监控列，并且是有变化的
                if (columeInfo.containsKey(column)) {
                    if (columeInfo.get(column).isChanged()) {
                        // 构建 RabbitMessage 对象
                        rabbitMessages.add(new RabbitMessage(message));
                        break;
                    }
                }
            }
        }
        return rabbitMessages;
    }

    /**
     * <p>
     * 提取目标字段
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

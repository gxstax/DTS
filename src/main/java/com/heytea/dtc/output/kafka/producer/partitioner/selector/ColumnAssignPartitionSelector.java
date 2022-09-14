package com.heytea.dtc.output.kafka.producer.partitioner.selector;

import com.heytea.dtc.constant.enums.RouteType;
import com.heytea.dtc.deliver.converters.TransferMessage;
import com.heytea.dtc.exception.DTCException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 功能描述
 * </p>
 *
 * @author Ant
 * @since 2022/1/13 11:26 上午
 */
@Slf4j
public class ColumnAssignPartitionSelector extends AbstractPartitionSelector {

    @Override
    public Object select(Object resource) {
        // 提取路由字段名称
        final String columnName = RouteType.extractColumnName(routeRule);
        if (ObjectUtils.isEmpty(columnName)) {
            log.warn("路由规则配置错误 routeRule[{}]", routeRule);
            throw new DTCException("路由规则配置错误 routeRule[" + routeRule + "]");
        }
        // 从数据中提取对应字段值
        TransferMessage transferMessage =  (TransferMessage) resource;
        final Map<String, TransferMessage.ColumnInfo> columeMap = transferMessage.getTransferData().getColume();
        final TransferMessage.ColumnInfo columnInfo = columeMap.get(columnName);
        final String columnValue = columnInfo.getBefore();

        // 根据字段值 hashCode 计算路由分区
        final List<PartitionInfo> partitionInfos = cluster.availablePartitionsForTopic(topic);
        return columnValue.hashCode() % partitionInfos.size();
    }

}

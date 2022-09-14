package com.heytea.dtc.spi;

import com.heytea.dtc.resources.mapping.kafka.DBMapping;

/**
 * <p>
 * Canal 与 Kafka 映射规则
 * </p>
 *
 * @author Ant
 * @since 2022/1/3 10:01 下午
 */
@Deprecated
public interface CanalKafkaMessageMappingRule {

    /**
     * <p>
     * 映射规则
     * </p>
     *
     * @param dbMapping
     * @return void
     */
    DBMapping mappingRule(DBMapping dbMapping);

}

package com.ant.dts.config.properties.kafka;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import java.util.Map;

/**
 * <p>
 * Kafka 客户端配置信息
 * </p>
 *
 * @author Ant
 * @since 2022/1/4 11:08 上午
 */
@Data
public class ConsumerProperties {

    /**
     * 消费者端配置信息（Map格式，便于和原生Kafka API 做映射）
     * <p>
     *     这里强烈建议您严格按照 kafka API 中的属性配置信息来一一映射
     *     所以我这里命名成了 kafka API 中的类名,具体配置请参照 {@link ConsumerConfig}
     *
     *     示例：如果我们要配置消费者每次拉取信息的最大条数
     *     那么您在配置文件中的配置(以properties文件格式为例)为：
     *     ----------------------------------------------------------------------------------
     *     heytea.data-transfer-center.kafka.consumer.consumer-config.[max.poll.records]=2000
     *     ----------------------------------------------------------------------------------
     *     其中 [max.poll.records] 这个key的名字便是取的 {@link ConsumerConfig#MAX_POLL_RECORDS_CONFIG} 对应的字符串信息
     * </p>
     *
     */
    private Map<String, Object> consumerConfig;

}
